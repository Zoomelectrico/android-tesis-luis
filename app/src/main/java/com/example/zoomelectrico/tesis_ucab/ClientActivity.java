package com.example.zoomelectrico.tesis_ucab;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.zoomelectrico.tesis_ucab.models.Encomienda;
import com.example.zoomelectrico.tesis_ucab.uihelpers.client.EncomiendaDialog;
import com.example.zoomelectrico.tesis_ucab.uihelpers.client.EncomiendasFragment;
import com.example.zoomelectrico.tesis_ucab.uihelpers.client.dummy.DummyContent;
import com.google.firebase.auth.FirebaseUser;

public class ClientActivity extends AppCompatActivity implements EncomiendasFragment.OnListFragmentInteractionListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        configSearchButton();
    }

    private void configSearchButton() {
        ((ImageButton) findViewById(R.id.btnSearch)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tracking = ((EditText) findViewById(R.id.txtTracking)).getText().toString();
                Encomienda encomienda = search(tracking);
                Log.e("Encomienda", tracking);
            }
        });
    }

    private Encomienda search(String track) {
        return null;
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {
        new EncomiendaDialog().show(getSupportFragmentManager(), "Hola");
    }
}
