package com.example.zoomelectrico.tesis_ucab;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.zoomelectrico.tesis_ucab.models.Administrador;
import com.example.zoomelectrico.tesis_ucab.models.Usuario;
import com.example.zoomelectrico.tesis_ucab.uihelpers.admin.TransporteFragment;
import com.example.zoomelectrico.tesis_ucab.uihelpers.admin.dummy.DummyContent;

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
                case R.id.navigation_dashboard:
                    intent = new Intent(context, AddEncomiendaAdminActivity.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
                    return true;
                case R.id.navigation_notifications:

                    return true;
            }
            return false;
        }
    };

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }
}
