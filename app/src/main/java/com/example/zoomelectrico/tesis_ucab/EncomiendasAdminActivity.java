package com.example.zoomelectrico.tesis_ucab;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.zoomelectrico.tesis_ucab.models.Encomienda;
import com.example.zoomelectrico.tesis_ucab.uihelpers.admin.TransporteFragment;
import com.example.zoomelectrico.tesis_ucab.uihelpers.client.EncomiendasFragment;

public class EncomiendasAdminActivity extends AppCompatActivity implements EncomiendasFragment.OnListFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encomiendas_admin);
    }

    @Override
    public void onListFragmentInteraction(Encomienda item) {

    }
}
