package com.example.zoomelectrico.tesis_ucab;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.zoomelectrico.tesis_ucab.models.Transporte;
import com.example.zoomelectrico.tesis_ucab.models.Usuario;
import com.example.zoomelectrico.tesis_ucab.uihelpers.admin.TransporteFragment;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class AdminActivity extends AppCompatActivity implements TransporteFragment.OnListFragmentInteractionListener {

    private Usuario user;
    private final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        user = Objects.requireNonNull(getIntent()).getParcelableExtra("user");
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    intent = new Intent(context, EncomiendasAdminActivity.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
                    return true;
                case R.id.navigation_encomiendas:
                    intent = new Intent(context, EncomiendasAdminActivity.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
                    return true;
                case R.id.navigation_new_encomindas:
                    intent = new Intent(context, AddEncomiendaAdminActivity.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
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

    @Override
    public void onListFragmentInteraction(Transporte item) {

    }
}
