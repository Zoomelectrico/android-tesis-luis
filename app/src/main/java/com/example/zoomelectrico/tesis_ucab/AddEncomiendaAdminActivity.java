package com.example.zoomelectrico.tesis_ucab;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.zoomelectrico.tesis_ucab.models.Encomienda;
import com.example.zoomelectrico.tesis_ucab.models.Usuario;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class AddEncomiendaAdminActivity extends AppCompatActivity {

    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    @NonNull
    private Context context = this;
    private Usuario user;
    private BottomNavigationView navigation;
    private SettingsClient settingsClient;
    private FusedLocationProviderClient locationProviderClient;
    @Nullable
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private LocationSettingsRequest locationSettingsRequest;
    private Location currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_encomienda_admin);
        permissionHandler();
        location();
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        user = Objects.requireNonNull(getIntent()).getParcelableExtra("user");
        findViewById(R.id.btnAddEncomienda).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.btnAddEncomienda).setVisibility(View.GONE);
                findViewById(R.id.pgAddEncomienda).setVisibility(View.VISIBLE);
                String emisor = ((EditText) findViewById(R.id.txtEmisorCI)).getText().toString();
                String receptor = ((EditText) findViewById(R.id.txtReceptorCI)).getText().toString();
                buildEncomienda(emisor, receptor);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigation.setSelectedItemId(R.id.navigation_new_encomindas);
        startLocationUpdates();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopLocationUpdates();
    }

    private void buildEncomienda(@NonNull final String e, @NonNull final String r) {
        final DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        final Context context = this;
        db.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String emisorID = "", receptorID = "";
                for (DataSnapshot user: dataSnapshot.getChildren()) {
                    String cedula = user.child("cedula").getValue(String.class);
                    if(e.equals(cedula)) {
                        emisorID = user.getKey();
                    } else if (r.equals(cedula)) {
                        receptorID = user.getKey();
                    }
                }
                String trackID = db.child("encomiendas").push().getKey();
                if(trackID != null) {
                    Encomienda e = new Encomienda(Calendar.getInstance().getTime().toString(),
                            receptorID, emisorID, 0, trackID);

                    db.child("encomiendas").child(trackID).setValue(e);
                    if (currentLocation == null) {
                        startLocationUpdates();
                    }
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("camion", "none");
                    map.put("latitud", currentLocation.getLatitude());
                    map.put("longitud", currentLocation.getLongitude());
                    HashMap<String, Double> lugar = new HashMap<>();
                    lugar.put("lat", currentLocation.getLatitude());
                    lugar.put("lon", currentLocation.getLongitude());
                    map.put("lugar", lugar);
                    db.child("ubicacionGPS").child(trackID).setValue(map);
                    findViewById(R.id.btnAddEncomienda).setVisibility(View.VISIBLE);
                    findViewById(R.id.pgAddEncomienda).setVisibility(View.GONE);
                    Toast.makeText(context, "Datos guardados", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Revise su conexion de internet", Toast.LENGTH_SHORT).show();
                    findViewById(R.id.btnAddEncomienda).setVisibility(View.VISIBLE);
                    findViewById(R.id.pgAddEncomienda).setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, "Revise su conexion de internet", Toast.LENGTH_SHORT).show();
                findViewById(R.id.btnAddEncomienda).setVisibility(View.VISIBLE);
                findViewById(R.id.pgAddEncomienda).setVisibility(View.GONE);
            }
        });
    }

    @NonNull
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    intent = new Intent(context, AdminActivity.class);
                    intent.putExtra("user", user);
                    intent.putParcelableArrayListExtra("transportes", Objects.requireNonNull(getIntent()).getParcelableArrayListExtra("transportes"));
                    startActivity(intent);
                    finish();
                    return true;
                case R.id.navigation_encomiendas:
                    intent = new Intent(context, EncomiendasAdminActivity.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
                    finish();
                    return true;
                case R.id.navigation_new_encomindas:
                    return true;
                case R.id.navigation_logout:
                    FirebaseAuth.getInstance().signOut();
                    intent = new Intent(context, LoadingActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
            }
            return false;
        }
    };

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
                                    rae.startResolutionForResult(AddEncomiendaAdminActivity.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Toast.makeText(AddEncomiendaAdminActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void stopLocationUpdates() {
        if(locationCallback != null) {
            this.locationProviderClient.removeLocationUpdates(this.locationCallback);
        }
    }

    private void permissionHandler() {
        if(!havePersmissions()) {
            askPemisions();
        }
    }

    private boolean havePersmissions() {
        return ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void askPemisions() {
        boolean rational = ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        if(!rational) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CHECK_SETTINGS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                if (grantResults.length <= 0) {

                } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
