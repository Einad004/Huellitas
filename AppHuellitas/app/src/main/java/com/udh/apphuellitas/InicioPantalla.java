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


        String usuario = getSharedPreferences(
                "HuellitasPrefs",
                MODE_PRIVATE
        ).getString("USUARIO", "");

        if (!usuario.isEmpty()) {
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
