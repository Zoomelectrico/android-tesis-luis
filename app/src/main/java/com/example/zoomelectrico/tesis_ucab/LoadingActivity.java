package com.example.zoomelectrico.tesis_ucab;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.zoomelectrico.tesis_ucab.models.Administrador;
import com.example.zoomelectrico.tesis_ucab.models.Encomienda;
import com.example.zoomelectrico.tesis_ucab.models.Transporte;
import com.example.zoomelectrico.tesis_ucab.models.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LoadingActivity extends AppCompatActivity {

    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener authListener;
    private final FirebaseDatabase db = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    loadUserData(user.getUid());
                } else {
                    findViewById(R.id.progressBarBig).setVisibility(View.GONE);
                    findViewById(R.id.layoutLogin).setVisibility(View.VISIBLE);
                }
            }
        };
        auth.addAuthStateListener(authListener);
        ((Button) findViewById(R.id.btnLogin)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.VISIBLE);
                ((Button) findViewById(R.id.btnLogin)).setVisibility(View.GONE);
                String email = ((EditText) findViewById(R.id.txtEmail)).getText().toString();
                String password = ((EditText) findViewById(R.id.txtPassword)).getText().toString();
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            loadUserData(task.getResult().getUser().getUid());
                        } else {
                            Log.w("LOGIN", task.getException());
                        }
                    }
                });
            }
        });
    }

    private void loadUserData(final String UID) {
        DatabaseReference ref = db.getReference("users").child(UID);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final Usuario user = dataSnapshot.getValue(Usuario.class);
                if(user != null) {
                    user.setUid(UID);
                    loadData(user, UID);
                } else {
                    goToFailback("El usuario de la DB es nulo");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Error", databaseError.getMessage());
            }
        });
    }

    private void goToFailback(String error) {
        Intent intent = new Intent(this, FailBackActivity.class);
        intent.putExtra("Error", error);
        startActivity(intent);
        finish();
    }


    private void loadData(@NonNull final Usuario user, final String UID) {
        switch (user.getTipo()) {
            case "cliente":
                db.getReference().child("encomiendas").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot encomiendasSnapshot) {
                        for(DataSnapshot encomienda: encomiendasSnapshot.getChildren()) {
                            try {
                                Encomienda e = encomienda.getValue(Encomienda.class);
                                if(e != null) {
                                    if(e.getRemitente().equals(UID) || e.getReceptor().equals(UID)) {
                                        user.addEncomienda(encomienda.getValue(Encomienda.class));
                                    }
                                }
                            } catch(ClassCastException e) {
                                Log.e("Error", e.getMessage());
                            }
                        }
                        goToActivity(user);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        goToFailback(databaseError.getMessage());
                    }
                });
                break;
            case "admin":
                db.getReference().addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        db.getReference().addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Administrador admin = new Administrador(user);
                                DataSnapshot encomiendasSnapshot = dataSnapshot.child("encomiendas");
                                DataSnapshot transporteSnapshot = dataSnapshot.child("transporte");
                                if(encomiendasSnapshot != null) {
                                    for(DataSnapshot encomienda: encomiendasSnapshot.getChildren()) {
                                        try {
                                            Encomienda e = encomienda.getValue(Encomienda.class);
                                            if(e != null) {
                                                admin.addEncomienda(encomienda.getValue(Encomienda.class));
                                            }
                                        } catch(ClassCastException e) {
                                            Log.e("Error", e.getMessage());
                                        }
                                    }
                                }
                                if(transporteSnapshot != null) {
                                    for(DataSnapshot transporte: transporteSnapshot.getChildren()) {
                                        Transporte t = transporte.getValue(Transporte.class);
                                        if (t != null) {
                                            t.setPlaca(transporte.getKey());
                                            admin.addTransporte(t);
                                        }
                                    }
                                }
                                goToActivity(admin);
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                goToFailback(databaseError.getMessage());
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                break;
            case "trabajador":
                goToActivity(user);
                break;
            default:

        }
    }

    private void goToActivity(@NonNull Usuario user) {
        Intent intent;
        switch (user.getTipo()) {
            case "cliente":
                intent = new Intent(this, ClientActivity.class);
                break;
            case "admin":
                intent = new Intent(this, AdminActivity.class);
                if(user instanceof Administrador) {
                    Administrador admin = (Administrador) user;
                    intent.putExtra("transportes", admin.getTransportes());
                }
                break;
            case "trabajador":
                intent = new Intent(this, WorkerActivity.class);
                break;
            default:
                intent = new Intent(this, FailBackActivity.class);
        }
        intent.putExtra("user", user);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        auth.removeAuthStateListener(authListener);
    }
}
