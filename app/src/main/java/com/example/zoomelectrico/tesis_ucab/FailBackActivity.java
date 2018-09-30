package com.example.zoomelectrico.tesis_ucab;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Objects;

public class FailBackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fail_back);
        String err = Objects.requireNonNull(getIntent()).getStringExtra("error");
        if (err != null) {
            String txt = "Hemos tenido un problema \n" + err;
            ((TextView) findViewById(R.id.txtFailBack)).setText(txt);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, LoadingActivity.class));
        finish();
    }
}
