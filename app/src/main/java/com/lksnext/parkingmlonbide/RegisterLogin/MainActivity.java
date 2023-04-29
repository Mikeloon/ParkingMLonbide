package com.lksnext.parkingmlonbide.RegisterLogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.lksnext.parkingmlonbide.DataClasses.User;
import com.lksnext.parkingmlonbide.NavFragments.HomePage;
import com.lksnext.parkingmlonbide.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView username = (TextView) findViewById(R.id.username);
        TextView password = (TextView) findViewById(R.id.password);

        MaterialButton loginbton = (MaterialButton) findViewById(R.id.loginbtn);

        loginbton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (username.getText().toString().equals(User.username) && password.getText().toString().equals(User.password)){
                    //correct
                    Toast.makeText(MainActivity.this, "Sesion Iniciada", Toast.LENGTH_SHORT).show();
                    openHomepage();
                }else
                    //incorrect
                    Toast.makeText(MainActivity.this, "Error al iniciar sesion", Toast.LENGTH_SHORT).show();
            }

        });
    }

    public void openHomepage(){
        Intent goToHome = new Intent(MainActivity.this, HomePage.class);
        startActivity(goToHome);
    }
}