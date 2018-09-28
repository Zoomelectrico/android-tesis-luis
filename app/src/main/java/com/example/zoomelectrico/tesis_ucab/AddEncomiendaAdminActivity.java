package com.example.zoomelectrico.tesis_ucab;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.zoomelectrico.tesis_ucab.models.Encomienda;
import com.example.zoomelectrico.tesis_ucab.models.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Objects;

public class AddEncomiendaAdminActivity extends AppCompatActivity {

    private Context context = this;
    private Usuario user;
    private BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_encomienda_admin);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        user = Objects.requireNonNull(getIntent()).getParcelableExtra("user");
        ((Button) findViewById(R.id.btnAddEncomienda)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Button) findViewById(R.id.btnAddEncomienda)).setVisibility(View.GONE);
                ((ProgressBar) findViewById(R.id.pgAddEncomienda)).setVisibility(View.VISIBLE);
                String emisor = ((EditText) findViewById(R.id.txtEmisorCI)).getText().toString();
                String receptor = ((EditText) findViewById(R.id.txtReceptorCI)).getText().toString();
                buildEncomienda(emisor, receptor);
                ((Button) findViewById(R.id.btnAddEncomienda)).setVisibility(View.VISIBLE);
                ((ProgressBar) findViewById(R.id.pgAddEncomienda)).setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigation.setSelectedItemId(R.id.navigation_new_encomindas);
    }

    private void buildEncomienda(final String e, final String r) {
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
                Encomienda e = new Encomienda(Calendar.getInstance().getTime().toString(),
                        receptorID, emisorID, 1, trackID);
                db.child("encomiendas").setValue(e);
                Toast.makeText(context, "Datos guardados", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, "Revise su conexion de internet", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.navigation_home:
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
}
