package com.lksnext.parkingmlonbide.RegisterLogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.lksnext.parkingmlonbide.DataClasses.User;
import com.lksnext.parkingmlonbide.R;

public class RegisterActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        TextView username = (TextView) findViewById(R.id.usernameR);
        TextView email = (TextView) findViewById(R.id.email);
        TextView pass = (TextView) findViewById(R.id.passwordR);
        TextView repPass = (TextView) findViewById(R.id.repetirpass);

        MaterialButton registerbton = (MaterialButton) findViewById(R.id.registerbton);
        registerbton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pass.getText().toString().equals(repPass.getText().toString()) && pass.getText().toString().length() > 5){
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                            email.getText().toString(),
                            pass.getText().toString()
                    ).addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            Intent goToLogin = new Intent(RegisterActivity.this,MainActivity.class);
                            Toast.makeText(RegisterActivity.this, "Registro completado", Toast.LENGTH_SHORT).show();
                            startActivity(goToLogin);
                        } else {
                            Toast.makeText(RegisterActivity.this, "Error al registrarse", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(RegisterActivity.this, "Error al registrarse", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}