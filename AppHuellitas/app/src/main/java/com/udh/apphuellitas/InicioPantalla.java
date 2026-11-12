package com.udh.apphuellitas;

import android.os.Bundle;

public class InicioPantalla extends MenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cargarContenido(R.layout.activity_inicio);
    }

    @Override
    protected int obtenerItemActual() {
        return R.id.nav_inicio;
    }
}
