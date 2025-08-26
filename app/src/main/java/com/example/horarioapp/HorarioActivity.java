package com.example.horarioapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.alamkanak.weekview.WeekView;
import com.google.android.material.navigation.NavigationView;

public class HorarioActivity extends AppCompatActivity{
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;
    private WeekView weekView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horario);

        // Vincular vistas
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        toolbar = findViewById(R.id.toolbar);
        weekView = findViewById(R.id.weekView);

        // Configurar toolbar como ActionBar
        setSupportActionBar(toolbar);

        // Configurar botón hamburguesa
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.black));

        // Manejo de clicks en el menú lateral
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();

                if (id == R.id.nav_pag_principal) {
                    Intent intent = new Intent(HorarioActivity.this, MainActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_courses) {
                    Toast.makeText(HorarioActivity.this, "Ingresar Cursos", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_calendario) {
                    Intent intent = new Intent(HorarioActivity.this, CalendarioActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_horario) {
                    Toast.makeText(HorarioActivity.this, "Horario", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_profesores) {
                    Toast.makeText(HorarioActivity.this, "Ingresa profesores", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_salir) {
                    finish();
                }

                drawerLayout.closeDrawers();
                return true;
            }
        });

        // Configuración del WeekView
        weekView.setNumberOfVisibleDays(3);
        weekView.setShowNowLine(true);
        weekView.setColumnGap(8);
        weekView.setHourHeight(60);
        weekView.setEventTextSize(12);
        weekView.setOverlappingEventGap(12);

        // Color de las filas de todos los días (gris)
        weekView.setPastBackgroundColor(Color.parseColor("#D3D3D3"));  // gris claro
        weekView.setFutureBackgroundColor(Color.parseColor("#D3D3D3")); // gris claro
        weekView.setTodayBackgroundColor(Color.parseColor("#A9A9A9"));  // gris medio
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(navigationView)) {
            drawerLayout.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }
}
