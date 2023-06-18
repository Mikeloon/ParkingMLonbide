package com.lksnext.parkingmlonbide.NavFragments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.lksnext.parkingmlonbide.DataClasses.User;
import com.lksnext.parkingmlonbide.RegisterLogin.MainActivity;
import com.lksnext.parkingmlonbide.R;

public class HomePage extends AppCompatActivity {
    private SharedPreferences preferences;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        DrawerLayout drawerLayout = findViewById(R.id.draweLayout);

        mAuth = FirebaseAuth.getInstance();

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
        TextView navEmail = (TextView) header.findViewById(R.id.emailHeader);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String userEmail = preferences.getString("email", "");
        navEmail.setText(userEmail);
        NavController navController = Navigation.findNavController(this, R.id.navHostFragment);
        NavigationUI.setupWithNavController(navigationView,navController);
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