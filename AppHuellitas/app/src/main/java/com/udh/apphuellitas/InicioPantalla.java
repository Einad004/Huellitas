package com.udh.apphuellitas;

import android.os.Bundle;
import android.widget.TextView;

public class InicioPantalla extends MenuActivity {

    private TextView txtBienvenida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cargarContenido(R.layout.activity_inicio);

        txtBienvenida = findViewById(R.id.txtBienvenida);

        // Recibir el usuario desde MainActivity
        String usuario = getIntent()
                .getStringExtra("USUARIO");

        if (usuario != null && !usuario.isEmpty()) {
            txtBienvenida.setText("Bienvenido, " + usuario);
        } else {
            txtBienvenida.setText("Bienvenido a Huellitas");
        }
    }

    @Override
    protected int obtenerItemActual() {
        return R.id.nav_inicio;
    }
}
