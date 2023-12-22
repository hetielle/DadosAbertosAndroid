package com.example.trabalhofinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Config extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return selectedItemNavigationView(item);
            }
        });
    }

    private boolean selectedItemNavigationView(MenuItem item){
        int idItem = item.getItemId();

        if (idItem == R.id.sair){
            startActivity(new Intent(this, Login.class));
        } else if (idItem == R.id.config) {
            startActivity(new Intent(this, Config.class));
        } else if (idItem == R.id.deputados) {
            startActivity(new Intent(this, HomeDeputados.class));
        } else if (idItem == R.id.partidos) {
            startActivity(new Intent(this, HomePartidos.class));
        }

        return false;
    }
}