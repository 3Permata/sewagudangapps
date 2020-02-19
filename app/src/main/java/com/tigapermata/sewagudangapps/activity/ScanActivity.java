package com.tigapermata.sewagudangapps.activity;

import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.tigapermata.sewagudangapps.AppController;
import com.tigapermata.sewagudangapps.R;

import java.util.List;

public class ScanActivity extends AppCompatActivity implements DecoratedBarcodeView.TorchListener{

    private CaptureManager capture;
    private DecoratedBarcodeView barcodeScannerView;
    private ImageButton switchFlashLightButton;
    private boolean isFlashLightOn = false;
    private boolean isScanDone = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        Toolbar toolbar = findViewById(R.id.toolbar_scan);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(AppController.getInstance().getTipeScan());

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
    }

    protected void resumeScanner() {
        isScanDone = false;
        if (!barcodeScannerView.isActivated())
            barcodeScannerView.resume();
        Log.d("peeyush-pause", "paused: false");
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
}
