package com.udh.apphuellitas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MenuActivity extends AppCompatActivity {

    private FrameLayout contenedor;
    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        contenedor = findViewById(R.id.contenedor);
        bottomNavigation =
                findViewById(R.id.bottomNavigation);

        bottomNavigation.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.nav_inicio) {
                cargarLayout(R.layout.activity_inicio);
                return true;

            } else if (id == R.id.nav_mascotas) {
                cargarLayout(R.layout.activity_mascotas);
                return true;

            } else if (id == R.id.nav_perfil) {
                cargarLayout(R.layout.activity_perfil);
                return true;
            }

            return false;
        });

        // Cargar la pantalla inicial
        int seleccionado = R.id.nav_inicio;

        if (savedInstanceState != null) {
            seleccionado = savedInstanceState.getInt(
                    "seccion", R.id.nav_inicio
            );
        }

        bottomNavigation.setSelectedItemId(seleccionado);

        // Mostrar la sección incluso si ya estaba seleccionada
        mostrarSeleccion(seleccionado);
    }

    private void mostrarSeleccion(int id) {
        if (id == R.id.nav_mascotas) {
            cargarLayout(R.layout.activity_mascotas);

        } else if (id == R.id.nav_perfil) {
            cargarLayout(R.layout.activity_perfil);

        } else {
            cargarLayout(R.layout.activity_inicio);
        }
    }

    private void cargarLayout(int layoutId) {

        contenedor.removeAllViews();

        LayoutInflater.from(this).inflate(
                layoutId,
                contenedor,
                true
        );
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(
                "seccion",
                bottomNavigation.getSelectedItemId()
        );
        super.onSaveInstanceState(outState);
    }
}
