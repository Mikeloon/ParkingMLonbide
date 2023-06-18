package com.lksnext.parkingmlonbide.RegisterLogin;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
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
import com.google.firebase.auth.FirebaseAuth;
import com.lksnext.parkingmlonbide.DataClasses.User;
import com.lksnext.parkingmlonbide.NavFragments.HomePage;
import com.lksnext.parkingmlonbide.R;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private int GOOGLE_SIGN_IN = 100;
    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        TextView emailEditText = (TextView) findViewById(R.id.userEmail);
        TextView passwordEditText = (TextView) findViewById(R.id.password);
        TextView returnRegister = (TextView) findViewById(R.id.goRegister);
        Button googleLoginBton = (Button) findViewById(R.id.googleSignInButton);

        TextView forgotPasswordEditText = (TextView) findViewById(R.id.forgotPassword);

        MaterialButton loginbton = (MaterialButton) findViewById(R.id.loginbtn);

        loginbton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signInWithEmailAndPassword(
                       emailEditText.getText().toString(),
                        passwordEditText.getText().toString()).addOnCompleteListener(task -> {
                            if (task.isSuccessful()){
                                saveEmailToSharedPreferences(emailEditText.getText().toString());
                                openHomepage();
                            } else {
                                Toast.makeText(MainActivity.this, "Error al iniciar sesion", Toast.LENGTH_SHORT).show();
                            }
                });
            }

        });

        googleLoginBton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
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
                    mAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            Toast.makeText(MainActivity.this, "Se ha enviado un correo electrónico para restablecer la contraseña", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "No se pudo enviar el correo electrónico para restablecer la contraseña", Toast.LENGTH_SHORT).show();
                        }
                    });
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
                                    saveEmailToSharedPreferences(user.getEmail());
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

    private void saveEmailToSharedPreferences(String email) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("email", email);
        editor.apply();
    }

    public void openHomepage(){
        Intent goToHome = new Intent(MainActivity.this, HomePage.class);
        startActivity(goToHome);
    }

}