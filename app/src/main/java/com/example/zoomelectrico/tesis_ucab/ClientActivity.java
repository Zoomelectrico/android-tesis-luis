package com.example.zoomelectrico.tesis_ucab;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.zoomelectrico.tesis_ucab.models.Encomienda;
import com.example.zoomelectrico.tesis_ucab.models.Usuario;
import com.example.zoomelectrico.tesis_ucab.uihelpers.client.EncomiendaDialog;
import com.example.zoomelectrico.tesis_ucab.uihelpers.client.EncomiendasFragment;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class ClientActivity extends AppCompatActivity implements EncomiendasFragment.OnListFragmentInteractionListener {

    private Usuario user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        configSearchButton();
        configFab();
        user = Objects.requireNonNull(getIntent()).getParcelableExtra("user");
        if(user != null ){
            Bundle bundle = new Bundle();
            bundle.putParcelable("user", user);
            EncomiendasFragment encomiendasFragment = new EncomiendasFragment();
            encomiendasFragment.setArguments(bundle);
        }

    }

    private void configSearchButton() {
        final Context context = this;
        findViewById(R.id.btnSearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tracking = ((EditText) findViewById(R.id.txtTracking)).getText().toString();
                Encomienda item = search(tracking);
                if (item != null) {
                    EncomiendaDialog dialog = new EncomiendaDialog();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("encomienda", item);
                    dialog.setArguments(bundle);
                    dialog.show(getSupportFragmentManager(), "Hola");
                } else {
                    Toast.makeText(context, "El codigo de Rastro no responde a ninguna encomienda", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void configFab() {
        findViewById(R.id.fabLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(ClientActivity.this, LoadingActivity.class);
                startActivity(i);
                ClientActivity.this.finish();
            }
        });
    }

    @Nullable
    private Encomienda search(String track) {
        Encomienda e = null;
        for(Encomienda encomienda: user.getEncomiendas()) {
            if(encomienda.getTrackingID().equals(track)){
                e = encomienda;
                break;
            }
        }
        return e;
    }

    @Override
    public void onListFragmentInteraction(Encomienda item) {
        EncomiendaDialog dialog = new EncomiendaDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelable("encomienda", item);
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(), "Hola");
    }
}
