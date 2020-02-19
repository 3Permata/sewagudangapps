package com.tigapermata.sewagudangapps.activity.inbound;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.zxing.ResultPoint;
import com.google.zxing.integration.android.IntentIntegrator;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.tigapermata.sewagudangapps.AppController;
import com.tigapermata.sewagudangapps.R;
import com.tigapermata.sewagudangapps.adapter.inbound.IncomingAdapterByItem;
import com.tigapermata.sewagudangapps.api.ApiClient;
import com.tigapermata.sewagudangapps.api.ApiService;
import com.tigapermata.sewagudangapps.helper.DBHelper;
import com.tigapermata.sewagudangapps.model.inbound.CekItemUpdate;
import com.tigapermata.sewagudangapps.model.inbound.CheckedListbyItem;
import com.tigapermata.sewagudangapps.model.inbound.StatusOK;
import com.tigapermata.sewagudangapps.utils.CustomEditText;
import com.tigapermata.sewagudangapps.utils.CustomTextView;
import com.tigapermata.sewagudangapps.utils.TitleTextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScanByItemActivity extends AppCompatActivity implements DecoratedBarcodeView.TorchListener {

    private CaptureManager capture;
    private DecoratedBarcodeView barcodeScannerView;
    private ImageButton switchFlashLightButton;
    private boolean isFlashLightOn = false;
    private boolean isScanDone = false;

    CustomTextView tvQtyAktual, tvQtyDokumen;
    String referensi, idItem, qtyAktual, qtyDokumen, idInbound, idIncoming;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbound_scan);

        Toolbar toolbar = findViewById(R.id.toolbar_scan_by_item);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Scan Item");

        tvQtyAktual = findViewById(R.id.total_qty_aktual_item);
        tvQtyDokumen = findViewById(R.id.total_qty_document_item);

        DBHelper dbHelper = new DBHelper(getApplicationContext());
        idInbound = dbHelper.getIdInbound().getIdInbound();
        idIncoming = dbHelper.getIdInbound().getIdIncoming();
        idItem = AppController.getInstance().getIdItem();
        qtyAktual = AppController.getInstance().getQtyAktual();
        qtyDokumen = AppController.getInstance().getQtyDokumen();
        referensi = AppController.getInstance().getReferensi();

        tvQtyAktual.setText(qtyAktual);
        tvQtyDokumen.setText(qtyDokumen);

        barcodeScannerView = findViewById(R.id.zxing_barcode_scanner);
        barcodeScannerView.setTorchListener(this);
        switchFlashLightButton = findViewById(R.id.switch_flashlight);

        if (!hasFlash()) {
            switchFlashLightButton.setVisibility(View.GONE);
        } else {
            switchFlashLightButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switchFlashLight();
                }
            });
        }

        barcodeScannerView.decodeContinuous(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                Toast.makeText(getApplicationContext(), result.getText(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void possibleResultPoints(List<ResultPoint> resultPoints) {

            }
        });

        //start capture
        capture = new CaptureManager(this, barcodeScannerView);
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        capture.decode();

    }

    private boolean hasFlash() {
        return getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    public void switchFlashLight() {
        if (isFlashLightOn) {
            barcodeScannerView.setTorchOff();
            isFlashLightOn = false;
        } else {
            barcodeScannerView.setTorchOn();
            isFlashLightOn = true;
        }
    }

    @Override
    public void onTorchOn() {
        Toast.makeText(getApplicationContext(), "Torch On", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTorchOff() {
        Toast.makeText(getApplicationContext(), "Torch Off", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
        resumeScanner();
        checkData();
    }

    protected void resumeScanner() {
        isScanDone = false;
        if (!barcodeScannerView.isActivated())
            barcodeScannerView.resume();
    }


    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeScannerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.scan_item_info, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_info:
                Intent intent = new Intent(ScanByItemActivity.this, ChecklistByItem.class);
                intent.putExtra("idItem", idItem);
                intent.putExtra("qtyAktual", qtyAktual);
                intent.putExtra("qtyDokumen", qtyDokumen);
                intent.putExtra("referensi", referensi);
                startActivity(intent);
                break;

            case R.id.action_create:
                addItem();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void addItem() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ScanByItemActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_item, null);
        final CustomEditText editLabel;
        editLabel = view.findViewById(R.id.tv_labels_by_item);

        builder.setView(view);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String label = editLabel.getText().toString();
                sendData(idItem, label);
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void sendData(final String idItem, String label) {
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
                if (response.isSuccessful()) {
                    String status = response.body().getStatus();
                    String error = "error";

                    if (status.equals(error)) {
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Berhasil", Toast.LENGTH_SHORT).show();
                        checkData();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Data Not Found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<StatusOK> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getApplicationContext(), "Please Check your internet connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkData() {
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        String idUser = dbHelper.getTokenn().getIdUser();
        String token = dbHelper.getTokenn().getToken();
        final String idGudang = dbHelper.getIds().getIdGudang();
        String idProject = dbHelper.getIds().getIdProject();


        ApiService service = ApiClient.getClient().create(ApiService.class);
        Call<CekItemUpdate> cekUpdateCall = service.cekItem(idGudang, idProject, idInbound, idUser, token, idItem);
        cekUpdateCall.enqueue(new Callback<CekItemUpdate>() {
            @Override
            public void onResponse(Call<CekItemUpdate> call, Response<CekItemUpdate> response) {
                if (response.isSuccessful()) {
                    tvQtyAktual.setText(response.body().getTotalQtyAktual());
                    tvQtyDokumen.setText(response.body().getTotalQtyDokumen());
                }
            }

            @Override
            public void onFailure(Call<CekItemUpdate> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getApplicationContext(), "Please Check your internet connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
