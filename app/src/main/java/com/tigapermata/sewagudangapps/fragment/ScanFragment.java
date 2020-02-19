package com.tigapermata.sewagudangapps.fragment;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.tigapermata.sewagudangapps.R;

public class ScanFragment extends Fragment implements DecoratedBarcodeView.TorchListener {

    private CaptureManager capture;
    private DecoratedBarcodeView barcodeScannerView;
    private ImageButton switchFlashLightButton;
    private boolean isFlashLightOn = false;

    public static ScanFragment newInstance() {
        ScanFragment fragment = new ScanFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_scan, container, false);

        barcodeScannerView = v.findViewById(R.id.zxing_barcode_scanner);
        barcodeScannerView.setTorchListener(ScanFragment.this);
        switchFlashLightButton = v.findViewById(R.id.switch_flashlight);

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

        //start capture
        capture = new CaptureManager(getActivity(), barcodeScannerView);
        capture.initializeFromIntent(getActivity().getIntent(), savedInstanceState);
        capture.decode();

        return v;
    }

    private boolean hasFlash() {
        return getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
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
        Toast.makeText(getActivity(), "Torch On", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTorchOff() {
        Toast.makeText(getActivity(), "Torch Off", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        capture.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        capture.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }

}
