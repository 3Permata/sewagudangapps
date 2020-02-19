package com.tigapermata.sewagudangapps.activity.inbound;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.tigapermata.sewagudangapps.AppController;
import com.tigapermata.sewagudangapps.R;
import com.tigapermata.sewagudangapps.adapter.inbound.ChecklistByItemAdapter;
import com.tigapermata.sewagudangapps.api.ApiClient;
import com.tigapermata.sewagudangapps.api.ApiService;
import com.tigapermata.sewagudangapps.helper.DBHelper;
import com.tigapermata.sewagudangapps.model.inbound.DataLabelItem;
import com.tigapermata.sewagudangapps.model.inbound.DataLabelItemList;
import com.tigapermata.sewagudangapps.model.inbound.StatusOK;
import com.tigapermata.sewagudangapps.model.putaway.StatusPutAway;
import com.tigapermata.sewagudangapps.utils.CustomTextView;
import com.tigapermata.sewagudangapps.utils.TitleTextView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChecklistByItem extends AppCompatActivity implements ChecklistByItemAdapter.ItemListener{

    TextView tQtyByItem, tNamaItem;
    EditText etLabelInput;
    ImageButton ibBarcodeScan;
    Button btnAddLabel;
    View vSemiTransparent;
    ProgressBar pbLoading;
    String idInbound, idIncoming, idItem, qtyAktual, qtyDokumen, refernsi;
    boolean onSendData = false;

    private ArrayList<DataLabelItem> dataLabelItemArrayList;
    private RecyclerView rvByItem;
    private ChecklistByItemAdapter byItemAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    Call<DataLabelItemList> dataLabelCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist_by_item);

        Toolbar toolbar = findViewById(R.id.toolbar_by_item);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Inbound - List Label");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tNamaItem = findViewById(R.id.textViewNamaItem);
        tQtyByItem = findViewById(R.id.textViewQtyChecklistByItem);
        etLabelInput = findViewById(R.id.editTextLabelByItem);
        etLabelInput.requestFocus();
        ibBarcodeScan = findViewById(R.id.imageButtonBarcodeScan);
        btnAddLabel = findViewById(R.id.buttonAddLabel);
        rvByItem = findViewById(R.id.rv_by_item);
        vSemiTransparent = findViewById(R.id.viewSemiTransparent);
        pbLoading = findViewById(R.id.progressBarLoading);

        DBHelper dbHelper = new DBHelper(getApplicationContext());
        idInbound = dbHelper.getIdInbound().getIdInbound();
        idIncoming = dbHelper.getIdInbound().getIdIncoming();

        idItem = AppController.getInstance().getIdItem();
        qtyAktual = AppController.getInstance().getQtyAktual();
        qtyDokumen = AppController.getInstance().getQtyDokumen();
        refernsi = AppController.getInstance().getReferensi();

        tQtyByItem.setText(qtyAktual + " / " + qtyDokumen);
        tNamaItem.setText(AppController.getInstance().getNamaItem());

        etLabelInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER && !etLabelInput.getText().toString().equals("")) {
                    incomingChecklist(etLabelInput.getText().toString(), idItem);
                    etLabelInput.setText(""); etLabelInput.requestFocus();
                    return true;
                }
                return false;
            }
        });

        btnAddLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etLabelInput.getText().toString().equals("")) {
                    incomingChecklist(etLabelInput.getText().toString(), idItem);
                    etLabelInput.setText(""); etLabelInput.requestFocus();
                }
                else
                    Toast.makeText(getApplicationContext(), "Field label kosong", Toast.LENGTH_SHORT).show();
            }
        });

        ibBarcodeScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(ChecklistByItem.this).setCaptureActivity(ScanByItemActivity.class);
                integrator.initiateScan();
            }
        });

        preparedDataChecked();
    }

    private void preparedDataChecked() {
        onLoading();
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        String idUser = dbHelper.getTokenn().getIdUser();
        String token = dbHelper.getTokenn().getToken();
        String idGudang = dbHelper.getIds().getIdGudang();
        String idProject = dbHelper.getIds().getIdProject();

        ApiService service = ApiClient.getClient().create(ApiService.class);
        dataLabelCheck = service.dataLabelCheck(idGudang, idProject, idInbound, idIncoming, idUser, token, idItem);
        dataLabelCheck.enqueue(new Callback<DataLabelItemList>() {
            @Override
            public void onResponse(Call<DataLabelItemList> call, Response<DataLabelItemList> response) {
                endLoading();
                if (response.isSuccessful()) {
                    dataLabelItemArrayList = response.body().getDataLabelItemArrayList();
                    if (dataLabelItemArrayList == null)
                        tQtyByItem.setText("0 / " + qtyDokumen);
                    else {
                        rvByItem.setHasFixedSize(true);
                        mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        rvByItem.setLayoutManager(mLayoutManager);
                        byItemAdapter = new ChecklistByItemAdapter(getApplicationContext(), dataLabelItemArrayList, ChecklistByItem.this);
                        rvByItem.setAdapter(byItemAdapter);
                        tQtyByItem.setText(dataLabelItemArrayList.size() + " / " + qtyDokumen);
                    }
                }
            }

            @Override
            public void onFailure(Call<DataLabelItemList> call, Throwable t) {
                endLoading();
                t.printStackTrace();
                if (!call.isCanceled())
                    AlertSuccess("Please Check your Internet Connection");
            }
        });
    }

    private void incomingChecklist(String label, final String idItem) {
        onLoading(); onSendData = true;
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        String idUser = dbHelper.getTokenn().getIdUser();
        String token = dbHelper.getTokenn().getToken();
        final String idGudang = dbHelper.getIds().getIdGudang();
        String idProject = dbHelper.getIds().getIdProject();

        ApiService service = ApiClient.getClient().create(ApiService.class);
        Call<StatusOK> sendDataLabel = service.sendDataLabel(idGudang, idProject, idInbound, idIncoming, idUser, token, idItem, label);
        sendDataLabel.enqueue(new Callback<StatusOK>() {
            @Override
            public void onResponse(Call<StatusOK> call, Response<StatusOK> response) {
                endLoading(); onSendData = false;
                if (response.isSuccessful()) {
                    String status = response.body().getStatus();

                    if (status.equals("error")) {
                        AlertSuccess("Label sudah ada");
                    }
                    else if (status.equals("full")) {
                        AlertSuccess("Data full");
                    }
                    else {
                        preparedDataChecked();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Gagal, coba lagi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<StatusOK> call, Throwable t) {
                endLoading(); onSendData = false;
                t.printStackTrace();
                AlertSuccess("Please Check your Internet Connection");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                String label = result.getContents();
                if (label.contains("!") || label.contains("@") || label.contains("#") || label.contains("$") ||
                        label.contains("%") || label.contains("^") || label.contains("&") || label.contains("*")
                        || label.contains("(") || label.contains(")") || label.contains("_") || label.contains("-")
                        || label.contains("+") || label.contains("=") || label.contains("{") || label.contains("[")
                        || label.contains("}") || label.contains("]") || label.contains("|") || label.contains(":")
                        || label.contains(";") || label.contains("'") || label.contains("<") || label.contains(",")
                        || label.contains(">") || label.contains(".") || label.contains("?") || label.contains("/")) {
                    AlertSuccess("Scan Again");
                } else {
                    incomingChecklist(label, idItem);
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void onLoading() {
        etLabelInput.setEnabled(false);
        btnAddLabel.setEnabled(false);
        ibBarcodeScan.setEnabled(false);
        pbLoading.setVisibility(View.VISIBLE);
        vSemiTransparent.setVisibility(View.VISIBLE);
    }

    private void endLoading() {
        etLabelInput.setEnabled(true);
        etLabelInput.requestFocus();
        btnAddLabel.setEnabled(true);
        ibBarcodeScan.setEnabled(true);
        pbLoading.setVisibility(View.INVISIBLE);
        vSemiTransparent.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataLabelCheck.cancel();
    }

    private void AlertSuccess(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ChecklistByItem.this);
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        if (!onSendData) {
            setResult(10);
            finish();
        }
    }

    @Override
    public void onItemClick(DataLabelItem item, int position) {
        //String label = item.getLabel();
        //deleteLabel(label);
    }

    private void deleteLabel(String label) {
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        String idUser = dbHelper.getTokenn().getIdUser();
        String token = dbHelper.getTokenn().getToken();
        String idGudang = dbHelper.getIds().getIdGudang();
        String idProject = dbHelper.getIds().getIdProject();

        ApiService service = ApiClient.getClient().create(ApiService.class);
        Call<StatusPutAway> deleteLabelCall = service.deleteLabel(idGudang, idProject, idInbound, idUser, token, idItem, label);
        deleteLabelCall.enqueue(new Callback<StatusPutAway>() {
            @Override
            public void onResponse(Call<StatusPutAway> call, Response<StatusPutAway> response) {
                if (response.isSuccessful()) {
                    AlertSuccess("Delete label successfully");
                    preparedDataChecked();
                }
            }

            @Override
            public void onFailure(Call<StatusPutAway> call, Throwable t) {
                AlertSuccess("Please Check your internet connection");
            }
        });
    }
}
