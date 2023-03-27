package com.example.internshiptask;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnFirebase = findViewById(R.id.btnFirebase);
        btnFirebase.setOnClickListener(view -> {
            Intent fb = new Intent(getApplicationContext(), FirebaseActivity.class);
            startActivity(fb);
        });

        Button btnSharedPref = findViewById(R.id.btnSharedPref);
        btnSharedPref.setOnClickListener(view -> {
            Intent fb = new Intent(getApplicationContext(), SharedPreferencesActivity.class);
            startActivity(fb);
        });
    }
}