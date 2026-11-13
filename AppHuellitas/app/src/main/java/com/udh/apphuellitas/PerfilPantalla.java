package com.udh.apphuellitas;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class PerfilPantalla extends MenuActivity {

    private Button btnCerrarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cargarContenido(R.layout.activity_perfil);

        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);

        getSharedPreferences("HuellitasPrefs", MODE_PRIVATE)
                .edit()
                .remove("USUARIO")
                .apply();

        btnCerrarSesion.setOnClickListener(v -> {
            Intent intent = new Intent(
                    PerfilPantalla.this,
                    MainActivity.class
            );
            startActivity(intent);
        });
    }
    @Override
    protected int obtenerItemActual() {
        return R.id.nav_perfil;
    }
}