package com.tigapermata.sewagudangapps.activity.outbound;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import com.tigapermata.sewagudangapps.adapter.outbound.FormOutgoingAdapter;
import com.tigapermata.sewagudangapps.api.ApiClient;
import com.tigapermata.sewagudangapps.api.ApiService;
import com.tigapermata.sewagudangapps.helper.DBHelper;
import com.tigapermata.sewagudangapps.model.inbound.DataLain;
import com.tigapermata.sewagudangapps.model.inbound.EditIncoming;
import com.tigapermata.sewagudangapps.model.inbound.RawFormIncoming;
import com.tigapermata.sewagudangapps.model.inbound.StatusOK;
import com.tigapermata.sewagudangapps.model.outbound.FormOutgoing;
import com.tigapermata.sewagudangapps.model.outbound.FormOutgoingList;
import com.tigapermata.sewagudangapps.utils.CustomTextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormOutgoingActivity extends AppCompatActivity {

    private RecyclerView rvForm;
    private RecyclerView.LayoutManager mLayoutManager;
    private FormOutgoingAdapter mAdapter;
    private Button btnSubmit;
    private TextView tvDate;
    private ImageView imgDate;
    private ProgressBar pbLoading;
    private View vSemiTransparent;

    private DatePickerDialog datePicker;
    private boolean onReqIncoming, onPostIncoming;

    private ArrayList<FormOutgoing> formOutgoingArrayList;
    private Call<FormOutgoingList> isiCall;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_outgoing);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Form Add Outgoing");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rvForm = findViewById(R.id.rv_form_outgoing);
        btnSubmit = findViewById(R.id.submit_input_outgoing);

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
                datePicker = new DatePickerDialog(FormOutgoingActivity.this, new DatePickerDialog.OnDateSetListener() {
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

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postOutgoingForm();
            }
        });

        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (onReqIncoming) isiCall.cancel();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        if (!onPostIncoming) {
            setResult(10);
            finish();
        }
    }

    private void init() {
        onLoading();
        onReqIncoming = true;
        formOutgoingArrayList = new ArrayList<>();

        DBHelper dbHelper = new DBHelper(getApplicationContext());
        String idUser = dbHelper.getTokenn().getIdUser();
        String token = dbHelper.getTokenn().getToken();
        String idGudang = dbHelper.getIds().getIdGudang();
        String idProject = dbHelper.getIds().getIdProject();

        ApiService service = ApiClient.getClient().create(ApiService.class);
        isiCall = service.listFormOutgoing(idGudang, idProject, idUser, token);
        isiCall.enqueue(new Callback<FormOutgoingList>() {
            @Override
            public void onResponse(Call<FormOutgoingList> call, Response<FormOutgoingList> response) {
                endLoading();
                onReqIncoming = false;
                if (response.isSuccessful()) {
                    formOutgoingArrayList = response.body().getFormIncomings();
                    rvForm.setHasFixedSize(true);
                    mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    rvForm.setLayoutManager(mLayoutManager);
                    mAdapter = new FormOutgoingAdapter(FormOutgoingActivity.this, formOutgoingArrayList, populateList());
                    rvForm.setAdapter(mAdapter);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Gagal, coba lagi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FormOutgoingList> call, Throwable t) {
                endLoading();
                t.printStackTrace();
                if (call.isCanceled())
                    AlertSuccess("Please Check your Internet Connection");
                onReqIncoming = false;
            }
        });
    }

    private ArrayList<EditIncoming> populateList() {
        ArrayList<EditIncoming> list = new ArrayList<>();

        if (formOutgoingArrayList != null) {
            for (int i = 0; i<formOutgoingArrayList.size(); i++) {
                EditIncoming editModel = new EditIncoming();
                editModel.setEditTextValue(String.valueOf(i));
                list.add(editModel);
            }
        }

        return list;
    }

    private ArrayList<DataLain> postList() {
        ArrayList<DataLain> list = new ArrayList<>();
        if (formOutgoingArrayList != null) {
            for (int i = 0; i<FormOutgoingAdapter.formOutgoingArrayList.size(); i++) {
                DataLain dataModel = new DataLain(FormOutgoingAdapter.formOutgoingArrayList.get(i).getLabel(),
                        FormOutgoingAdapter.editOutgoingArrayList.get(i).getEditTextValue());
                list.add(dataModel);
            }
        }

        return list;
    }

    private void postOutgoingForm() {
        onPostIncoming = true;
        onLoading();
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        String idUser = dbHelper.getTokenn().getIdUser();
        String token = dbHelper.getTokenn().getToken();
        String idGudang = dbHelper.getIds().getIdGudang();
        String idProject = dbHelper.getIds().getIdProject();
        String tanggal = tvDate.getText().toString();
        String idOutbound = AppController.getInstance().getIdOutbound();

        RawFormIncoming rawForm = new RawFormIncoming(idUser, token, tanggal, postList());
        ApiService service = ApiClient.getClient().create(ApiService.class);
        Call<StatusOK> outgoingStatusCall = service.addOutgoing(idGudang, idProject, idOutbound, rawForm);
        outgoingStatusCall.enqueue(new Callback<StatusOK>() {
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
                endLoading(); onPostIncoming = false;
                t.printStackTrace();
                AlertSuccess("Please Check your Internet Connection");
            }
        });
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
        AlertDialog.Builder builder = new AlertDialog.Builder(FormOutgoingActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_not_found_data, null);
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
