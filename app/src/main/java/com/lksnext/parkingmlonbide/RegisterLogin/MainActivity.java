package com.lksnext.parkingmlonbide.RegisterLogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.lksnext.parkingmlonbide.DataClasses.User;
import com.lksnext.parkingmlonbide.NavFragments.HomePage;
import com.lksnext.parkingmlonbide.R;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView emailEditText = (TextView) findViewById(R.id.username);
        TextView passwordEditText = (TextView) findViewById(R.id.password);
        TextView returnRegister = (TextView) findViewById(R.id.goRegister);

        MaterialButton loginbton = (MaterialButton) findViewById(R.id.loginbtn);

        loginbton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(
                       emailEditText.getText().toString(),
                        passwordEditText.getText().toString()).addOnCompleteListener(task -> {
                            if (task.isSuccessful()){
                                openHomepage();
                            } else {
                                Toast.makeText(MainActivity.this, "Error al iniciar sesion", Toast.LENGTH_SHORT).show();
                            }
                });
            }

        });

        returnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToRegister = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(goToRegister);
            }
        });
    }
    public void openHomepage(){
        Intent goToHome = new Intent(MainActivity.this, HomePage.class);
        startActivity(goToHome);
    }
}