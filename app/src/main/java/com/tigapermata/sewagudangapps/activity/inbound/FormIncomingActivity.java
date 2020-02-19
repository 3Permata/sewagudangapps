package com.tigapermata.sewagudangapps.activity.inbound;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tigapermata.sewagudangapps.AppController;
import com.tigapermata.sewagudangapps.R;
import com.tigapermata.sewagudangapps.adapter.inbound.FormIncomingAdapter;
import com.tigapermata.sewagudangapps.api.ApiClient;
import com.tigapermata.sewagudangapps.api.ApiService;
import com.tigapermata.sewagudangapps.helper.DBHelper;
import com.tigapermata.sewagudangapps.model.inbound.DataLain;
import com.tigapermata.sewagudangapps.model.inbound.EditIncoming;
import com.tigapermata.sewagudangapps.model.inbound.FormIncoming;
import com.tigapermata.sewagudangapps.model.inbound.FormIncomingList;
import com.tigapermata.sewagudangapps.model.inbound.StatusOK;
import com.tigapermata.sewagudangapps.model.inbound.RawFormIncoming;
import com.tigapermata.sewagudangapps.utils.CustomTextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormIncomingActivity extends AppCompatActivity {

    private ArrayList<FormIncoming> formIncomingArrayList;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView rvForm;
    private FormIncomingAdapter mAdapter;
    private Button btn;
    private TextView tvDate;
    private ImageView imgDate;
    private DatePickerDialog datePicker;
    private boolean onReqIncoming, onPostIncoming;
    private ProgressBar pbLoading;
    private View vSemiTransparent;
    private Call<FormIncomingList> isiCall;

    View view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_incoming);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Form Add Incoming");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rvForm = findViewById(R.id.rv_form_incoming);
        btn = findViewById(R.id.submit_input_incoming);

        tvDate = findViewById(R.id.tanggal);
        imgDate = findViewById(R.id.imageViewTanggal);
        pbLoading = findViewById(R.id.progressBarLoading);
        vSemiTransparent = findViewById(R.id.viewSemiTransparent);

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String dateString = format.format(Calendar.getInstance().getTime());
        tvDate.setText(dateString);
        imgDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                datePicker = new DatePickerDialog(FormIncomingActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth){
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, monthOfYear, dayOfMonth);
                        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                        String dateString = format.format(calendar.getTime());
                        tvDate.setText(dateString);
                    }
                }, mYear, mMonth, mDay);
                datePicker.show();
            }
        });

        init();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postIncomingForm();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (onReqIncoming) isiCall.cancel();
    }

    @Override
    public void onBackPressed() {
        if (!onPostIncoming) {
            setResult(10);
            finish();
        }
    }

    //request form incoming from server
    private void init() {
        onLoading();
        onReqIncoming = true;
        formIncomingArrayList = new ArrayList<>();

        DBHelper dbHelper = new DBHelper(getApplicationContext());
        String idUser = dbHelper.getTokenn().getIdUser();
        String token = dbHelper.getTokenn().getToken();
        String idGudang = dbHelper.getIds().getIdGudang();
        String idProject = dbHelper.getIds().getIdProject();

        ApiService service = ApiClient.getClient().create(ApiService.class);
        isiCall = service.listFormIncoming(idGudang, idProject, idUser, token);
        isiCall.enqueue(new Callback<FormIncomingList>() {
            @Override
            public void onResponse(Call<FormIncomingList> call, Response<FormIncomingList> response) {
                endLoading();
                onReqIncoming = false;
                if (response.isSuccessful()) {
                    formIncomingArrayList = response.body().getFormIncomings();
                    rvForm.setHasFixedSize(true);
                    mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    rvForm.setLayoutManager(mLayoutManager);
                    mAdapter = new FormIncomingAdapter(FormIncomingActivity.this, formIncomingArrayList, populateList());
                    rvForm.setAdapter(mAdapter);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Gagal, coba lagi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FormIncomingList> call, Throwable t) {
                endLoading();
                t.printStackTrace();
                if (call.isCanceled())
                    AlertSuccess("Please Check your Internet Connection");
                onReqIncoming = false;
            }
        });
    }

    //show the list of incoming form
    private ArrayList<EditIncoming> populateList() {
        ArrayList<EditIncoming> list = new ArrayList<>();

        if (formIncomingArrayList != null) {
            for (int i = 0; i<formIncomingArrayList.size(); i++) {
                EditIncoming editModel = new EditIncoming();
                editModel.setEditTextValue(String.valueOf(i));
                list.add(editModel);
            }
        }

        return list;
    }

    //send data to server with form that has been requested
    private void postIncomingForm() {
        onPostIncoming = true;
        onLoading();
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        String idUser = dbHelper.getTokenn().getIdUser();
        String token = dbHelper.getTokenn().getToken();
        String idGudang = dbHelper.getIds().getIdGudang();
        String idProject = dbHelper.getIds().getIdProject();
        String tanggal = tvDate.getText().toString();
        String idInbound = AppController.getInstance().getIdInbound();

        RawFormIncoming rawForm = new RawFormIncoming(idUser, token, tanggal, postList());
        ApiService service = ApiClient.getClient().create(ApiService.class);
        Call<StatusOK> incomingStatusCall = service.addIncoming(idGudang, idProject, idInbound, rawForm);
        incomingStatusCall.enqueue(new Callback<StatusOK>() {
            @Override
            public void onResponse(Call<StatusOK> call, Response<StatusOK> response) {
                onPostIncoming = false;
                endLoading();
                if (response.isSuccessful()) {
                    setResult(10);
                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Gagal, coba lagi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<StatusOK> call, Throwable t) {
                endLoading();
                t.printStackTrace();
                AlertSuccess("Please Check your Internet Connection");
                onPostIncoming = false;
            }
        });

    }

    //get data from the form that has been requested
    private ArrayList<DataLain> postList() {
        ArrayList<DataLain> list = new ArrayList<>();
        if (formIncomingArrayList != null) {
            for (int i = 0; i<FormIncomingAdapter.formIncomingArrayList.size(); i++) {
                DataLain dataModel = new DataLain(FormIncomingAdapter.formIncomingArrayList.get(i).getLabel(),
                        FormIncomingAdapter.editIncomingArrayList.get(i).getEditTextValue());
                list.add(dataModel);
            }
        }

        return list;
    }

    private void onLoading() {
        vSemiTransparent.setVisibility(View.VISIBLE);
        pbLoading.setVisibility(View.VISIBLE);
    }

    private void endLoading() {
        vSemiTransparent.setVisibility(View.INVISIBLE);
        pbLoading.setVisibility(View.INVISIBLE);
    }

    private void AlertSuccess(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(FormIncomingActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_not_found_data, null);
        CustomTextView textDialog;
        textDialog = view.findViewById(R.id.text_dialog);
        textDialog.setText(message);
        textDialog.setTextSize(18);


        builder.setView(view);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
