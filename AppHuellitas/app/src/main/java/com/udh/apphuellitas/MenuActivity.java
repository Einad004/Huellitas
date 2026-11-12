
package com.udh.apphuellitas;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public abstract class MenuActivity extends AppCompatActivity {

    // Contenedor de las pantallas
    private FrameLayout contenedor;

    // Barra inferior compartida
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

            if (id == obtenerItemActual()) {
                return true;
            }

            Intent intent;

            if (id == R.id.nav_inicio) {

                intent = new Intent(
                        this,
                        InicioPantalla.class
                );

            } else if (id == R.id.nav_mascotas) {

                intent = new Intent(
                        this,
                        MascotasPantalla.class
                );

            } else if (id == R.id.nav_perfil) {

                intent = new Intent(
                        this,
                        PerfilPantalla.class
                );

            } else {
                return false;
            }

            // Navegar a la pantalla seleccionada
            intent.addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_SINGLE_TOP
            );

            startActivity(intent);

            return true;
        });

        bottomNavigation.getMenu()
                .findItem(obtenerItemActual())
                .setChecked(true);
    }

    // Cada pantalla indica su opción de navegación
    protected abstract int obtenerItemActual();

    // Cargar el XML de la pantalla correspondiente
    protected void cargarContenido(int layoutId) {

        // Limpiar contenido anterior
        contenedor.removeAllViews();

        // Insertar el layout dentro del contenedor
        LayoutInflater.from(this).inflate(
                layoutId,
                contenedor,
                true
        );
    }
}
