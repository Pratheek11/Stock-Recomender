package com.example.stockrecomender;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class Afterload extends AppCompatActivity {
    Button getit, knowmore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afterload);
        getit = findViewById(R.id.getit);
        knowmore = findViewById(R.id.knowmore);

        //this takes us to chrome tab for learning more about "Market"
        knowmore.setOnClickListener(v -> {
            Toast.makeText(getBaseContext(), "Varsity app by Zerodha", Toast.LENGTH_LONG).show();
            Uri uri = Uri.parse("https://zerodha.com/varsity/");
            Intent lm = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(lm);
        });

        //this is used to call api and load data
        getit.setOnClickListener(v -> {
            Intent it = new Intent(Afterload.this, Showstock.class);
            startActivity(it);
        });

    }
}