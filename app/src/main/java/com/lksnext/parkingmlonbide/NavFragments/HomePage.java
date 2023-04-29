package com.lksnext.parkingmlonbide.NavFragments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.lksnext.parkingmlonbide.DataClasses.User;
import com.lksnext.parkingmlonbide.RegisterLogin.MainActivity;
import com.lksnext.parkingmlonbide.R;

public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        DrawerLayout drawerLayout = findViewById(R.id.draweLayout);
        findViewById(R.id.imgMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        NavigationView navigationView = findViewById(R.id.navV);
        navigationView.setItemIconTintList(null);

        View header = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) header.findViewById(R.id.userHeader);
        TextView navEmail = (TextView) header.findViewById(R.id.emailHeader);
        navUsername.setText(User.username);
        navEmail.setText(User.email);
        NavController navController = Navigation.findNavController(this, R.id.navHostFragment);
        NavigationUI.setupWithNavController(navigationView,navController);
    }

    public void openLogInActivity(){
        Intent gotoLogin = new Intent(HomePage.this, MainActivity.class);
        startActivity(gotoLogin);
    }
}