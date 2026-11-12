package com.udh.apphuellitas;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MascotasPantalla extends MenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cargarContenido(R.layout.activity_mascotas);

    }
    @Override
    protected int obtenerItemActual() {
        return R.id.nav_mascotas;
    }
}