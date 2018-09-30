package com.example.zoomelectrico.tesis_ucab;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class WorkerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler, ActivityCompat.OnRequestPermissionsResultCallback {

    private final int REQUEST_CODE_CAMARA = 34;
    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    @Nullable
    private ZXingScannerView mScannerView;
    private FloatingActionButton fabAdd, fabLogout, fabGoback;
    private Animation fabOpen, fabClose, fabRotateClockwise, fabRotateCounter;
    private boolean isOpen = false;
    private SettingsClient settingsClient;
    private FusedLocationProviderClient locationProviderClient;
    @Nullable
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private LocationSettingsRequest locationSettingsRequest;
    private Location currentLocation;


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
    public void handleResult(@NonNull Result rawResult) {
        String string = rawResult.getText();
        if(string != null ){
            Log.e("Results", string);
            mScannerView.stopCamera();
            setContentView(R.layout.activity_worker);
            configUI(string);
        }
    }

    private void location() {
        this.settingsClient = LocationServices.getSettingsClient(this);
        this.locationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        createLocationCallback();
        createLocationRequest();
        buildLocationSettingsRequest();
        startLocationUpdates();
    }

    private void createLocationCallback() {
        this.locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                currentLocation = locationResult.getLastLocation();
            }
        };
    }

    private void createLocationRequest() {
        this.locationRequest = new LocationRequest();
        this.locationRequest.setInterval(2000);
        this.locationRequest.setFastestInterval(1000);
        this.locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(this.locationRequest);
        this.locationSettingsRequest = builder.build();
    }

    private void startLocationUpdates() {
        this.settingsClient.checkLocationSettings(this.locationSettingsRequest)
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        try {
                            locationProviderClient.requestLocationUpdates(locationRequest, Objects.requireNonNull(locationCallback), Looper.myLooper());
                        } catch (SecurityException e) {
                            Log.e("Main", "Security Exception", e);
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                try {
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(WorkerActivity.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i("", "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e("", errorMessage);
                                Toast.makeText(WorkerActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    /**
     * Metodo stopLocationUpdates: este metodo se encarga de terminar de escuchar los cambios de ubicación en el telefono
     */
    private void stopLocationUpdates() {
        if(locationCallback != null) {
            this.locationProviderClient.removeLocationUpdates(this.locationCallback);
        } else {
            Log.e("Location Callback", "IS NULL");
        }
    }

    private void configUI(@NonNull final String datos) {
        fabConfig();
        location();
        final String[] statuses = new String[] {
                "Entrando al Sistema",
                "Entregado al Destinatario en Oficina",
                "Recibido en Oficina de Transito",
                "Recibido en Oficina de Destino",
                "Enviado en Transporte",
                "Entregado al Destinatario en Direccion",
                "Usuario no Encontrado",
                "Transporte de Entrega"
        };
        final Integer[] pos = new Integer[1];
        FirebaseDatabase.getInstance()
                .getReference("encomiendas/" + datos)
                .child("status")
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ((TextView) findViewById(R.id.txtIdWorker)).setText(datos);
                Integer i = dataSnapshot.getValue(int.class);
                if (i != null) {
                    ((TextView) findViewById(R.id.txtStatusWorker)).setText(statuses[i]);
                } else {
                    ((TextView) findViewById(R.id.txtStatusWorker)).setText("Desconocido");
                }
                findViewById(R.id.btnChangeStatus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (pos[0] == null) {
                            Toast.makeText(WorkerActivity.this, "Spinner", Toast.LENGTH_SHORT).show();
                        } else {
                            FirebaseDatabase.getInstance().getReference("encomiendas/" + datos + "/status").setValue(pos[0], new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError databaseError, @NonNull final DatabaseReference databaseReference) {
                                    if (databaseError != null) {
                                        Log.e("Error", databaseError.getMessage());
                                        Toast.makeText(WorkerActivity.this, "Error DB", Toast.LENGTH_SHORT).show();
                                    } else {
                                        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("ubicacionGPS").child(datos).child("lugar");
                                        HashMap<String, Double> lugar = new HashMap<>();
                                        lugar.put("lat", currentLocation.getLatitude());
                                        lugar.put("lon", currentLocation.getLongitude());
                                        ref.setValue(lugar);
                                        stopLocationUpdates();
                                        setContentView(mScannerView);
                                        mScannerView.setResultHandler(WorkerActivity.this);
                                        mScannerView.startCamera();
                                    }
                                }
                            });
                        }
                    }
                });
                Spinner spinner = findViewById(R.id.spinnerStatus);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(WorkerActivity.this, R.layout.spinner_item, statuses);
                spinner.setAdapter(adapter);
                spinner.setContentDescription("Estado del Paquete");
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        pos[0] = position;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Error", databaseError.getMessage());
            }
        });


    }

    private void fabConfig() {
        fabAdd = findViewById(R.id.fabAdd);
        fabLogout = findViewById(R.id.fabLogoutWorker);
        fabGoback = findViewById(R.id.fabBackWorker);
        fabOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        fabRotateClockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_clockwise);
        fabRotateCounter = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_counterclockwise);
        fabLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(WorkerActivity.this, LoadingActivity.class);
                startActivity(intent);
                finish();
            }
        });
        fabGoback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(mScannerView);
                mScannerView.setResultHandler(WorkerActivity.this);
                mScannerView.startCamera();

            }
        });

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOpen) {
                    fabAdd.startAnimation(fabRotateCounter);
                    fabLogout.startAnimation(fabClose);
                    fabGoback.startAnimation(fabClose);
                    fabLogout.setClickable(false);
                    fabGoback.setClickable(false);
                    isOpen = false;
                } else {
                    fabAdd.startAnimation(fabRotateClockwise);
                    fabLogout.startAnimation(fabOpen);
                    fabGoback.startAnimation(fabOpen);
                    fabLogout.setClickable(true);
                    fabGoback.setClickable(true);
                    isOpen = true;
                }
            }
        });
    }

    private void permissionHandler() {
        if(!havePersmissions()) {
            askPemisions();
        }
    }

    private boolean havePersmissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void askPemisions() {
        boolean rational = ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA);
        if(!rational) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_CAMARA);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        stopLocationUpdates();
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, LoadingActivity.class));
        finish();
    }
}
