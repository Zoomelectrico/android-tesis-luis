package com.example.zoomelectrico.tesis_ucab;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;


import java.util.ArrayList;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class WorkerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler, ActivityCompat.OnRequestPermissionsResultCallback {

    private final int REQUEST_CODE_CAMARA = 34;
    private ZXingScannerView mScannerView;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        permissionHandler();
        mScannerView = new ZXingScannerView(this);
        ArrayList<BarcodeFormat> formats = new ArrayList<>();
        formats.add(BarcodeFormat.QR_CODE);
        mScannerView.setFormats(formats);
        mScannerView.setAutoFocus(true);
        setContentView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        Log.e("Result", rawResult.getText());
        Log.e("Result", rawResult.getBarcodeFormat().toString());
        mScannerView.resumeCameraPreview(this);
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
