package com.lksnext.parkingmlonbide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;

public class Inicio extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TipoEstacionamiento e1 = TipoEstacionamiento.NORMAL;
        TipoEstacionamiento e2 = TipoEstacionamiento.CARGADORELEC;
        TipoEstacionamiento e3 = TipoEstacionamiento.MINUSVALIDOS;
        TipoEstacionamiento e4 = TipoEstacionamiento.MOTOS;

        Plaza p1 = new Plaza("Plaza1",e1,false);
        Plaza p2 = new Plaza("Plaza2",e1,false);
        Plaza p3 = new Plaza("Plaza3",e2,false);
        Plaza p4 = new Plaza("Plaza4",e3,false);
        Plaza p5 = new Plaza("Plaza5",e4,false);
        Plaza p6 = new Plaza("Plaza6",e4,false);
        Plaza p7 = new Plaza("Plaza7",e2,false);

        Parking.plazasParking.add(p1);
        Parking.plazasParking.add(p2);
        Parking.plazasParking.add(p3);
        Parking.plazasParking.add(p4);
        Parking.plazasParking.add(p5);
        Parking.plazasParking.add(p6);
        Parking.plazasParking.add(p7);

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