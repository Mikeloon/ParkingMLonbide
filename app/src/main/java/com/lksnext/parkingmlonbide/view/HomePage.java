package com.lksnext.parkingmlonbide.view;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.lksnext.parkingmlonbide.R;

public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseAuth mAuth;

    private FirebaseFirestore db;
    private DrawerLayout drawerLayout;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        DrawerLayout drawerLayout = findViewById(R.id.draweLayout);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        findViewById(R.id.imgMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        NavigationView navigationView = findViewById(R.id.navV);
        navigationView.setItemIconTintList(null);

        Menu menu = navigationView.getMenu();
        MenuItem logoutMenuItem = menu.findItem(R.id.Salir);

        logoutMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                logout();
                return true;
            }
        });

        View header = navigationView.getHeaderView(0);

        TextView navUsername = (TextView) header.findViewById(R.id.userHeader);
        TextView navEmail = (TextView) header.findViewById(R.id.emailHeader);



        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("users").document(uid).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            // El documento existe, puedes acceder a los campos
                            String username = documentSnapshot.getString("name");
                            String email = documentSnapshot.getString("email");
                            String role = documentSnapshot.getString("role");
                            if (role != null && role.equals("Admin")) {
                                // Mostrar la opción "Pruebas"
                                Menu menu = navigationView.getMenu();
                                menu.findItem(R.id.menuParking).setVisible(false);
                                menu.findItem(R.id.menuReserva).setVisible(false);
                                menu.findItem(R.id.menuAdmin).setVisible(true);
                            } else {
                                // Ocultar la opción "Pruebas"
                                Menu menu = navigationView.getMenu();
                                menu.findItem(R.id.menuAdmin).setVisible(false);
                            }
                            navUsername.setText(username);
                            navEmail.setText(email);
                            // Utiliza los campos como desees
                            Log.d(TAG, "Nombre: " + username);
                            Log.d(TAG, "Correo electrónico: " + email);
                        } else {
                            // El documento no existe o aún no se ha creado
                            Log.d(TAG, "El documento no existe");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Ocurrió un error al obtener el documento
                        Log.e(TAG, "Error al obtener el documento del usuario", e);
                    }

                });

        NavController navController = Navigation.findNavController(this, R.id.navHostFragment);
        NavigationUI.setupWithNavController(navigationView,navController);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        int itemId = item.getItemId();
        if (itemId == R.id.menuProfile) {
            navController.navigate(R.id.profileFragment);
        } else if (itemId == R.id.menuParking) {
            navController.navigate(R.id.ParkingFragment);
        } else if (itemId == R.id.menuReserva) {
            navController.navigate(R.id.BookingFragment);
        } else if (itemId == R.id.Salir) {
            logout();
        }  else if (itemId == R.id.menuAdmin){
            navController.navigate(R.id.AdminFragment);
        }
        return true;
    }

    private void logout() {
        // Cerrar sesión en Firebase Auth
        mAuth.signOut();
        openLogInActivity();
        finish(); // Opcionalmente, puedes finalizar la actividad actual para evitar que el usuario vuelva atrás desde la pantalla de inicio de sesión
    }

    public void openLogInActivity(){
        Intent gotoLogin = new Intent(HomePage.this, MainActivity.class);
        startActivity(gotoLogin);
    }
}