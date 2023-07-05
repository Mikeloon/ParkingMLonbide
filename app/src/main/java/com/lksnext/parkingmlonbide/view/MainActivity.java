package com.lksnext.parkingmlonbide.view;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.lksnext.parkingmlonbide.R;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private int GOOGLE_SIGN_IN = 100;
    private FirebaseFirestore db;

    public static final String MSG_CONST = "Bienvenido";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        TextView emailEditText = (TextView) findViewById(R.id.userEmail);
        TextView passwordEditText = (TextView) findViewById(R.id.password);
        TextView returnRegister = (TextView) findViewById(R.id.goRegister);
        ImageView googleLoginBton = (ImageView) findViewById(R.id.googleSignInButton);

        TextView forgotPasswordEditText = (TextView) findViewById(R.id.forgotPassword);

        MaterialButton loginbton = (MaterialButton) findViewById(R.id.loginbtn);

        loginbton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!emailEditText.getText().toString().isEmpty() && !passwordEditText.getText().toString().isEmpty()){
                    mAuth.signInWithEmailAndPassword(
                            emailEditText.getText().toString(),
                            passwordEditText.getText().toString()).addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            Toast.makeText(MainActivity.this, MSG_CONST + " " + emailEditText.getText().toString().split("@")[0] + "!", Toast.LENGTH_SHORT).show();
                            openHomepage();
                        } else {
                            Toast.makeText(MainActivity.this, "Error al iniciar sesion", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else{
                    Toast.makeText(MainActivity.this, "Rellene los campos correctamente", Toast.LENGTH_SHORT).show();
                }
            }

        });

        googleLoginBton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken("763306789736-9e3e7iumrht4mp0aafrpmesg64pakmoh.apps.googleusercontent.com")
                        .requestEmail()
                        .build();
                GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(MainActivity.this, gso);
                startActivityForResult(googleSignInClient.getSignInIntent(), GOOGLE_SIGN_IN);
            }
        });

        forgotPasswordEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEditText.getText().toString();
                if (TextUtils.isEmpty(email)){
                    Toast.makeText(MainActivity.this, "Por favor, ingresa tu correo electrónico", Toast.LENGTH_SHORT).show();
                } else {
                    sendResetPassEmail(email);
                }
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

    private void SaveUserInFirestore(String name, FirebaseUser user, String uid){
        db.collection("users").document(uid).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "Usuario ya registrado.");
                            } else {
                                Map<String, Object> userMap = new HashMap<>();
                                userMap.put("name", name);
                                userMap.put("email", user.getEmail());
                                userMap.put("role","User");

                                db.collection("users").document(uid).set(userMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(MainActivity.this, MSG_CONST + " " + name + "!", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.d(TAG, "Error al guardar el usuario en Firestore");
                                            }
                                        });
                            }
                        } else {
                            Log.d(TAG, "Error al verificar la existencia del usuario en Firestore");
                        }
                    }
                });
    }

    private void sendResetPassEmail(String email){
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Toast.makeText(MainActivity.this, "Se ha enviado un correo electrónico para restablecer la contraseña", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "No se pudo enviar el correo electrónico para restablecer la contraseña", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Obtener la cuenta de Google
                GoogleSignInAccount account = task.getResult(ApiException.class);

                // Autenticar con Firebase usando el token de ID de Google
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Autenticación con cuenta de Google exitosa
                            FirebaseUser user = mAuth.getCurrentUser();
                            String uid = mAuth.getCurrentUser().getUid();
                            String name = user.getDisplayName();
                            SaveUserInFirestore(name, user, uid);
                            Toast.makeText(MainActivity.this, MSG_CONST + " " + name + "!", Toast.LENGTH_SHORT).show();
                            openHomepage();
                        } else {
                            // Fallo en la autenticación con cuenta de Google
                            Toast.makeText(MainActivity.this, "Error al iniciar sesión con cuenta de Google", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } catch (ApiException e) {
                // Error al obtener la cuenta de Google
                Toast.makeText(MainActivity.this, "Error al obtener la cuenta de Google", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void openHomepage(){
        Intent goToHome = new Intent(MainActivity.this, HomePage.class);
        startActivity(goToHome);
    }

}