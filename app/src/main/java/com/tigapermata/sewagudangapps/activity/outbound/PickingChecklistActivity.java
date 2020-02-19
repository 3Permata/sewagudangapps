package com.tigapermata.sewagudangapps.activity.outbound;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.tigapermata.sewagudangapps.AppController;
import com.tigapermata.sewagudangapps.R;
import com.tigapermata.sewagudangapps.activity.ScanActivity;
import com.tigapermata.sewagudangapps.adapter.outbound.PickingChecklistAdapter;
import com.tigapermata.sewagudangapps.api.ApiClient;
import com.tigapermata.sewagudangapps.api.ApiService;
import com.tigapermata.sewagudangapps.helper.DBHelper;
import com.tigapermata.sewagudangapps.model.outbound.DataLabelItemPicked;
import com.tigapermata.sewagudangapps.model.outbound.DataPickingChecklist;
import com.tigapermata.sewagudangapps.model.outbound.StatusPicking;
import com.tigapermata.sewagudangapps.model.putaway.StatusPutAway;
import com.tigapermata.sewagudangapps.utils.CustomTextView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PickingChecklistActivity extends AppCompatActivity implements PickingChecklistAdapter.PickingChecklistListener {

    private ArrayList<DataLabelItemPicked> dataLabelItemArrayList;
    private RecyclerView rvByItem;
    private PickingChecklistAdapter byItemAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private TextView tQtyByItem, tNamaItem;
    private EditText etLabelInput;
    private ImageButton ibBarcodeScan;
    private Button btnAddLabel;
    private View vSemiTransparent;
    private ProgressBar pbLoading;

    private boolean onSendData = false;
    private String idItem, idOutbound;

    private Call<DataPickingChecklist> dataLabelCheck;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_picking_checklist);

        Toolbar toolbar = findViewById(R.id.toolbar_by_item);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Picking - List Label");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String namaItem = AppController.getInstance().getNamaItem();
        idItem = AppController.getInstance().getIdItem();
        idOutbound = AppController.getInstance().getIdOutbound();

        tNamaItem = findViewById(R.id.textViewNamaItem);
        tNamaItem.setText(namaItem);
        tQtyByItem = findViewById(R.id.textViewQtyChecklistByItem);
        etLabelInput = findViewById(R.id.editTextLabelByItem);
        etLabelInput.requestFocus();
        ibBarcodeScan = findViewById(R.id.imageButtonBarcodeScan);
        btnAddLabel = findViewById(R.id.buttonAddLabel);
        rvByItem = findViewById(R.id.rv_by_item);
        vSemiTransparent = findViewById(R.id.viewSemiTransparent);
        pbLoading = findViewById(R.id.progressBarLoading);

        etLabelInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER && !etLabelInput.getText().toString().equals("")) {
                    checkLabel(etLabelInput.getText().toString());
                    etLabelInput.setText("");
                    return true;
                }
                return false;
            }
        });

        btnAddLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etLabelInput.getText().toString().equals("")) {
                    checkLabel(etLabelInput.getText().toString());
                }
                else
                    Toast.makeText(getApplicationContext(), "Field label kosong", Toast.LENGTH_SHORT).show();
            }
        });

        ibBarcodeScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppController.getInstance().setTipeScan("Scan Label");
                IntentIntegrator integrator = new IntentIntegrator(PickingChecklistActivity.this).setCaptureActivity(ScanActivity.class);
                integrator.initiateScan();
            }
        });

        preparedDataChecked();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                String label = result.getContents();
                checkLabel(label);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onItemPickingChecklistClicked(DataLabelItemPicked itemPicked, int position) {
        cancelPicking(itemPicked.getIdOutboundDetail());
    }

    private void preparedDataChecked() {
        onLoading();
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        String idUser = dbHelper.getTokenn().getIdUser();
        String token = dbHelper.getTokenn().getToken();
        String idGudang = dbHelper.getIds().getIdGudang();
        String idProject = dbHelper.getIds().getIdProject();

        ApiService service = ApiClient.getClient().create(ApiService.class);
        dataLabelCheck = service.showListLabelPicked(idGudang, idProject, idOutbound, idUser, token, idItem);
        dataLabelCheck.enqueue(new Callback<DataPickingChecklist>() {
            @Override
            public void onResponse(Call<DataPickingChecklist> call, Response<DataPickingChecklist> response) {
                endLoading();
                if (response.isSuccessful()) {
                    dataLabelItemArrayList = response.body().getDataLabelItemPickedArrayList();
                    if (dataLabelItemArrayList == null)
                        tQtyByItem.setText("0 / " + response.body().getTotalPicking());
                    else {
                        rvByItem.setHasFixedSize(true);
                        mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        rvByItem.setLayoutManager(mLayoutManager);
                        byItemAdapter = new PickingChecklistAdapter(getApplicationContext(), dataLabelItemArrayList, PickingChecklistActivity.this);
                        rvByItem.setAdapter(byItemAdapter);
                        tQtyByItem.setText(response.body().getTotalScanned() + " / " + response.body().getTotalPicking());
                    }
                }
                else {
                    Log.i("asd", "fail");
                }
            }

            @Override
            public void onFailure(Call<DataPickingChecklist> call, Throwable t) {
                endLoading();
                t.printStackTrace();
                if (!call.isCanceled())
                    AlertSuccess("Please Check your Internet Connection");
            }
        });
    }

    private void checkLabel(String label) {
        onLoading(); onSendData = true;
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        String idUser = dbHelper.getTokenn().getIdUser();
        String token = dbHelper.getTokenn().getToken();
        String idGudang = dbHelper.getIds().getIdGudang();
        String idProject = dbHelper.getIds().getIdProject();

        ApiService service = ApiClient.getClient().create(ApiService.class);
        Call<StatusPicking> sendDataLabel = service.scanItemLabelPicking(idGudang, idProject, idOutbound, idUser, token, label, idItem);
        sendDataLabel.enqueue(new Callback<StatusPicking>() {
            @Override
            public void onResponse(Call<StatusPicking> call, Response<StatusPicking> response) {
                endLoading(); onSendData = false;
                if (response.isSuccessful()) {
                    String info = response.body().getInfo();

                    if (info.matches("success")) {
                        preparedDataChecked();
                        Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        AlertSuccess(info);
                    }

                    String picking = response.body().getTotalPicking();
                    String scanned = response.body().getTotalScanned();
                    if (picking.matches(scanned)) {
                        setResult(11);
                        finish();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Gagal, coba lagi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<StatusPicking> call, Throwable t) {
                endLoading(); onSendData = false;
                t.printStackTrace();
                AlertSuccess("Please Check your Internet Connection");
            }
        });
    }

    private void cancelPicking(String idOutboundDetail) {
        onLoading(); onSendData = true;
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        String idUser = dbHelper.getTokenn().getIdUser();
        String token = dbHelper.getTokenn().getToken();
        String idGudang = dbHelper.getIds().getIdGudang();
        String idProject = dbHelper.getIds().getIdProject();

        ApiService service = ApiClient.getClient().create(ApiService.class);
        Call<StatusPutAway> cancelPickingCall = service.cancelPicking(idGudang, idProject, idOutbound, idUser, token, idOutboundDetail);
        cancelPickingCall.enqueue(new Callback<StatusPutAway>() {
            @Override
            public void onResponse(Call<StatusPutAway> call, Response<StatusPutAway> response) {
                endLoading(); onSendData = false;
                if (response.isSuccessful()) {
                    String status = response.body().getStatus();

                    if (status.matches("berhasil")) {
                        preparedDataChecked();
                        Toast.makeText(getApplicationContext(), "cancel success", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        AlertSuccess("Item tidak ada");
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Gagal, coba lagi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<StatusPutAway> call, Throwable t) {
                endLoading(); onSendData = false;
                t.printStackTrace();
                AlertSuccess("Please Check your Internet Connection");
            }
        });
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
        AlertDialog.Builder builder = new AlertDialog.Builder(PickingChecklistActivity.this);
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
