package com.lksnext.parkingmlonbide.RegisterLogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.lksnext.parkingmlonbide.R;

public class Inicio extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        MaterialButton registbtton = (MaterialButton) findViewById(R.id.registrarsebtton);
        MaterialButton logbtton = (MaterialButton) findViewById(R.id.logearsesbtton);

        registbtton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRegisterpage();
            }
        });

        logbtton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLoginpage();
            }
        });
    }

    public void openRegisterpage(){
        Intent goToHome = new Intent(Inicio.this,RegisterActivity.class);
        startActivity(goToHome);
    }

    public void openLoginpage(){
        Intent goToHome = new Intent(Inicio.this,MainActivity.class);
        startActivity(goToHome);
    }
}