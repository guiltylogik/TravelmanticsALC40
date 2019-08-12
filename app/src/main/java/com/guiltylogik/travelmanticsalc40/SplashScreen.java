package com.guiltylogik.travelmanticsalc40;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.guiltylogik.travelmanticsalc40.ui.DealsListActivity;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent splash = new Intent(this, DealsListActivity.class);
        startActivity(splash);
        finish();
    }
}
