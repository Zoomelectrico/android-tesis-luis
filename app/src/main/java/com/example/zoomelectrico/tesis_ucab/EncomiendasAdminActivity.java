package com.example.zoomelectrico.tesis_ucab;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.zoomelectrico.tesis_ucab.models.Encomienda;
import com.example.zoomelectrico.tesis_ucab.models.Transporte;
import com.example.zoomelectrico.tesis_ucab.models.Usuario;
import com.example.zoomelectrico.tesis_ucab.uihelpers.admin.TransporteFragment;
import com.example.zoomelectrico.tesis_ucab.uihelpers.client.EncomiendaDialog;
import com.example.zoomelectrico.tesis_ucab.uihelpers.client.EncomiendasFragment;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Objects;

public class EncomiendasAdminActivity extends AppCompatActivity implements EncomiendasFragment.OnListFragmentInteractionListener{

    private Context context = this;
    private Usuario user;
    private BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encomiendas_admin);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        user = Objects.requireNonNull(getIntent()).getParcelableExtra("user");
        if(user != null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("user", user);
            EncomiendasFragment encomiendasFragment = new EncomiendasFragment();
            encomiendasFragment.setArguments(bundle);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigation.setSelectedItemId(R.id.navigation_encomiendas);
    }

    @Override
    public void onListFragmentInteraction(Encomienda item) {
        EncomiendaDialog dialog = new EncomiendaDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelable("encomienda", item);
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(), "Hola");
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
                    return true;
                case R.id.navigation_new_encomindas:
                    intent = new Intent(context, AddEncomiendaAdminActivity.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
                    finish();
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
