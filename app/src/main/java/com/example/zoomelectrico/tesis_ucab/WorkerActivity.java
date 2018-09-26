package com.example.zoomelectrico.tesis_ucab;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

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
    protected void onDestroy() {
        super.onDestroy();
        mScannerView.stopCamera();
        mScannerView = null;

    }

    @Override
    public void handleResult(Result rawResult) {
        String string = rawResult.getText();
        if(string != null ){
            Log.e("Results", string);
            mScannerView.stopCamera();
            String[] datos = string.split(";");
            setContentView(R.layout.activity_worker);
            configUI(datos);
        }
        // mScannerView.resumeCameraPreview(this);
    }

    private void configUI(String[] datos) {
        String[] statuses = new String[] {
                "Entrando al Sistema",
                "Entregado al Destinatario en Oficina",
                "Recibido en Oficina de Transito",
                "Recibido en Oficina de Destino",
                "Enviado en Transporte",
                "Entregado al Destinatario en Direccion",
                "Usuario no Encontrado",
                "Transporte de Entrega"
        };
        Log.e("datos",datos.toString());
        ((TextView) findViewById(R.id.txtIdWorker)).setText(datos[0]);
        ((TextView) findViewById(R.id.txtStatusWorker)).setText(statuses[1]);
        Spinner spinner = ((Spinner) findViewById(R.id.spinnerStatus));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, statuses);
        spinner.setAdapter(adapter);

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
