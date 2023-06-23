package com.lksnext.parkingmlonbide.RegisterLogin;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.lksnext.parkingmlonbide.DataClasses.User;
import com.lksnext.parkingmlonbide.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

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
                            String uid = mAuth.getCurrentUser().getUid();

                            Map<String, Object> userMap = new HashMap<>();
                            userMap.put("name", username.getText().toString());
                            userMap.put("email", email.getText().toString());
                            userMap.put("reservas", new ArrayList<>());
                            userMap.put("role","User");
                            db.collection("users").document(uid).set(userMap)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "Usuario almacenado en firestore correctamente");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d(TAG, "Error al guardar el usuario en Firestore");
                                        }
                                    });

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