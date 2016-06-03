package com.luminoussquares.myappportfolio;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void sendMessageCapstone(View view) {
        Toast.makeText(getApplicationContext(), "This button will launch my Capstone app", Toast.LENGTH_SHORT).show();
    }

    public void sendMessageGoUbiquitous(View view) {
        Toast.makeText(getApplicationContext(), "This button will launch my Go Ubiquitous app", Toast.LENGTH_SHORT).show();
    }

    public void sendMessagePopularMovies(View view) {
        Toast.makeText(getApplicationContext(), "This button will launch my Popular Movies app", Toast.LENGTH_SHORT).show();
    }

    public void sendMessageStockHawk(View view) {
        Toast.makeText(getApplicationContext(), "This button will launch my Stock Hawk app", Toast.LENGTH_SHORT).show();
    }

    public void sendMessageBuildItBigger(View view) {
        Toast.makeText(getApplicationContext(), "This button will launch my Build It Bigger app", Toast.LENGTH_SHORT).show();
    }

    public void sendMessageMakeYourAppMaterial(View view) {
        Toast.makeText(getApplicationContext(), "This button will launch my Make Your App Material app", Toast.LENGTH_SHORT).show();
    }
}
