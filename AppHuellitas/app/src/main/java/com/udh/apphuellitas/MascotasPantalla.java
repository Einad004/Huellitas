package com.udh.apphuellitas;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MascotasPantalla extends MenuActivity {

    private Button btnRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cargarContenido(R.layout.activity_mascotas);

        btnRegistrar = findViewById(R.id.btnRegistrar);

        btnRegistrar.setOnClickListener(v->{
            Intent intent = new Intent(
                    MascotasPantalla.this,
                    Registro.class
            );
            startActivity(intent);

        });


    }
    @Override
    protected int obtenerItemActual() {
        return R.id.nav_mascotas;
    }
}