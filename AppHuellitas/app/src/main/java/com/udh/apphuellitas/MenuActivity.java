package com.udh.apphuellitas;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.ComponentActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MenuActivity extends ComponentActivity {

    private TextView txtSeccion;
    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        txtSeccion = findViewById(R.id.txtSeccion);
        bottomNavigation = findViewById(R.id.bottomNavigation);

        bottomNavigation.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.nav_inicio) {
                mostrarSeccion(
                        "Inicio",
                        "Bienvenido a Huellitas"
                );
                return true;

            } else if (id == R.id.nav_mascotas) {
                mostrarSeccion(
                        "Mascotas",
                        "Explora nuestras mascotas"
                );
                return true;

            } else if (id == R.id.nav_perfil) {
                mostrarSeccion(
                        "Perfil",
                        "Información de tu cuenta"
                );
                return true;
            }

            return false;
        });

        bottomNavigation.setSelectedItemId(R.id.nav_inicio);
    }

    private void mostrarSeccion(
            String titulo,
            String descripcion
    ) {
        txtSeccion.setText(titulo);
    }
}