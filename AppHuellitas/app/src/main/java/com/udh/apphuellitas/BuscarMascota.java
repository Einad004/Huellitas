package com.udh.apphuellitas;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.udh.apphuellitas.model.AppDatabase;
import com.udh.apphuellitas.model.Mascota;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class BuscarMascota extends MenuActivity {

    private EditText etBuscarId;
    private TextView txtResultado;
    private Button btnBuscarId;

    private AppDatabase db;
    private ExecutorService io;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cargarContenido(R.layout.activity_buscar_mascota);

        etBuscarId = findViewById(R.id.etBuscarId);
        txtResultado = findViewById(R.id.txtResultado);
        btnBuscarId = findViewById(R.id.btnBuscarId);

        db = AppDatabase.getInstance(this);
        io = Executors.newSingleThreadExecutor();

        btnBuscarId.setOnClickListener(v -> buscarMascota());
    }

    private void buscarMascota() {

        String id = etBuscarId.getText()
                .toString().trim();

        if (id.isEmpty()) {
            etBuscarId.setError("Ingrese un ID");
            return;
        }

        txtResultado.setText("Buscando...");

        io.execute(() -> {

            try {
                Mascota mascota = db.mascotaDao().buscarPorId(id);

                runOnUiThread(() -> {

                    if (mascota != null) {

                        txtResultado.setText(
                                "Nombre: " + mascota.getNombre() +
                                        "\nEspecie: " + mascota.getEspecie() +
                                        "\nRaza: " + mascota.getRaza() +
                                        "\nNacimiento: " +
                                        mascota.getFechaNacimiento()
                        );

                    } else {
                        txtResultado.setText(
                                "No se encontró ninguna mascota"
                        );
                    }
                });

            } catch (Exception e) {
                runOnUiThread(() ->
                        txtResultado.setText(
                                "Error al consultar la base de datos"
                        )
                );
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (io != null) {
            io.shutdown();
        }
        super.onDestroy();
    }

    @Override
    protected int obtenerItemActual() {
        return R.id.nav_mascotas;
    }
}