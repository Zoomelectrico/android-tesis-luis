package com.example.zoomelectrico.tesis_ucab;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.edwardvanraak.materialbarcodescanner.MaterialBarcodeScanner;
import com.edwardvanraak.materialbarcodescanner.MaterialBarcodeScannerBuilder;
import com.google.android.gms.vision.barcode.Barcode;

public class WorkerActivity extends AppCompatActivity {

    private final int REQUEST_CODE_CAMARA = 34;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker);
        permissionHandler();
    }

    private void startScan() {
        final MaterialBarcodeScanner materialBarcodeScanner = new MaterialBarcodeScannerBuilder()
                .withActivity(WorkerActivity.this)
                .withEnableAutoFocus(true)
                .withBleepEnabled(true)
                .withBackfacingCamera()
                .withCenterTracker()
                .withText("Scanning...")
                .withResultListener(new MaterialBarcodeScanner.OnResultListener() {
                    @Override
                    public void onResult(Barcode barcode) {
                        Log.e("Barcode", barcode.rawValue);
                    }
                })
                .build();
        materialBarcodeScanner.startScan();
    }

    private void permissionHandler() {
        if(!havePersmissions()) {
            askPemisions();
        }
    }

    private boolean havePersmissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void askPemisions() {
        boolean rational = ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA);
        if(!rational) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMARA);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_CAMARA:
                if (grantResults.length <= 0) {
                    Log.i("", "User interaction was cancelled.");
                } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("Per", "Good");
                }
                break;
        }
    }
}
