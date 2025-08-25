package com.example.horarioapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Vincular vistas
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        toolbar = findViewById(R.id.toolbar);

        // Configurar toolbar como ActionBar
        setSupportActionBar(toolbar);

        // Configurar botón hamburguesa
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.black));

        // Eventos del menú lateral
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();

                if (id == R.id.nav_pag_principal) {
                    Toast.makeText(MainActivity.this, "Abrir Horario", Toast.LENGTH_SHORT).show();

                } else if (id == R.id.nav_calendario) {
                    Intent intent = new Intent(MainActivity.this, CalendarioActivity.class);
                    startActivity(intent);

                } else if (id == R.id.nav_courses) {
                    Toast.makeText(MainActivity.this, "Ingresar Cursos", Toast.LENGTH_SHORT).show();

                } else if (id == R.id.nav_config) {
                    Toast.makeText(MainActivity.this, "Abrir Configuración", Toast.LENGTH_SHORT).show();

                } else if (id == R.id.nav_salir) {
                    finish();
                }

                drawerLayout.closeDrawers();
                return true;
            }
        });
    }
}
