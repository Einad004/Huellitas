
package com.udh.apphuellitas;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.udh.apphuellitas.model.AppDatabase;
import com.udh.apphuellitas.model.Mascota;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BuscarMascota extends MenuActivity {

    private EditText etBuscarId;
    private TextView txtResultado;

    private Button btnBuscarId;
    private Button btnEditarMascota;
    private Button btnEliminarMascota;

    private AppDatabase db;
    private ExecutorService io;

    private Mascota mascotaEncontrada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cargarContenido(R.layout.activity_buscar_mascota);

        etBuscarId = findViewById(R.id.etBuscarId);
        txtResultado = findViewById(R.id.txtResultado);

        btnBuscarId = findViewById(R.id.btnBuscarId);
        btnEditarMascota =
                findViewById(R.id.btnEditarMascota);
        btnEliminarMascota =
                findViewById(R.id.btnEliminarMascota);

        db = AppDatabase.getInstance(this);
        io = Executors.newSingleThreadExecutor();

        btnBuscarId.setOnClickListener(v -> buscarMascota());

        btnEditarMascota.setOnClickListener(v -> {

            if (mascotaEncontrada == null) return;

            Intent intent = new Intent(
                    BuscarMascota.this,
                    Registro.class
            );

            intent.putExtra(
                    "ID_MASCOTA",
                    mascotaEncontrada.getIdMascota()
            );

            startActivity(intent);
        });

        btnEliminarMascota.setOnClickListener(v -> {

            if (mascotaEncontrada != null) {
                confirmarEliminacion();
            }
        });
    }

    // ===================================
    // BUSCAR MASCOTA POR ID
    // ===================================

    private void buscarMascota() {

        String id = etBuscarId.getText()
                .toString().trim();

        mascotaEncontrada = null;

        btnEditarMascota.setVisibility(View.GONE);
        btnEliminarMascota.setVisibility(View.GONE);

        if (id.isEmpty()) {
            etBuscarId.setError("Ingrese un ID");
            return;
        }

        txtResultado.setText("Buscando...");

        io.execute(() -> {

            try {

                Mascota mascota =
                        db.mascotaDao().buscarPorId(id);

                runOnUiThread(() -> {

                    // Comprobar que pertenece al usuario
                    SharedPreferences prefs =
                            getSharedPreferences(
                                    "HuellitasPrefs",
                                    MODE_PRIVATE
                            );

                    String usuario =
                            prefs.getString("USUARIO", "");

                    if (mascota != null &&
                            !usuario.isEmpty() &&
                            usuario.equals(
                                    mascota.getIdUsuario()
                            )) {

                        mascotaEncontrada = mascota;

                        txtResultado.setText(
                                "Nombre: " +
                                        mascota.getNombre() +
                                        "\nEspecie: " +
                                        mascota.getEspecie() +
                                        "\nRaza: " +
                                        mascota.getRaza() +
                                        "\nNacimiento: " +
                                        mascota.getFechaNacimiento()
                        );

                        btnEditarMascota.setVisibility(
                                View.VISIBLE
                        );

                        btnEliminarMascota.setVisibility(
                                View.VISIBLE
                        );

                    } else {

                        txtResultado.setText(
                                "Mascota no encontrada"
                        );
                    }
                });

            } catch (Exception e) {

                runOnUiThread(() ->
                        txtResultado.setText(
                                "Error al consultar Room"
                        )
                );
            }
        });
    }

    // ===================================
    // CONFIRMAR ELIMINACIÓN
    // ===================================

    private void confirmarEliminacion() {

        new AlertDialog.Builder(this)
                .setTitle("Eliminar mascota")
                .setMessage(
                        "¿Deseas eliminar este registro?"
                )
                .setPositiveButton(
                        "Eliminar",
                        (dialog, which) ->
                                eliminarMascota()
                )
                .setNegativeButton(
                        "Cancelar",
                        null
                )
                .show();
    }

    // ===================================
    // ELIMINAR DE ROOM
    // ===================================

    private void eliminarMascota() {

        if (mascotaEncontrada == null) return;

        Mascota mascota = mascotaEncontrada;

        btnEliminarMascota.setEnabled(false);
        btnEditarMascota.setEnabled(false);

        io.execute(() -> {

            try {

                int filas = db.mascotaDao()
                        .eliminar(mascota);

                runOnUiThread(() -> {

                    if (filas > 0) {

                        mascotaEncontrada = null;

                        txtResultado.setText(
                                "Mascota eliminada correctamente"
                        );

                        etBuscarId.setText("");

                        btnEditarMascota.setVisibility(
                                View.GONE
                        );

                        btnEliminarMascota.setVisibility(
                                View.GONE
                        );

                    } else {

                        txtResultado.setText(
                                "No se encontró el registro para eliminar"
                        );
                    }

                    btnEliminarMascota.setEnabled(true);
                    btnEditarMascota.setEnabled(true);
                });

            } catch (Exception e) {

                runOnUiThread(() -> {

                    txtResultado.setText(
                            "Error al eliminar la mascota"
                    );

                    btnEliminarMascota.setEnabled(true);
                    btnEditarMascota.setEnabled(true);
                });
            }
        });
    }

    // Actualizar al regresar desde Registro
    @Override
    protected void onResume() {
        super.onResume();

        if (mascotaEncontrada != null) {
            etBuscarId.setText(
                    mascotaEncontrada.getIdMascota()
            );
            buscarMascota();
        }
    }

    @Override
    protected void onDestroy() {
        io.shutdown();
        super.onDestroy();
    }

    @Override
    protected int obtenerItemActual() {
        return R.id.nav_mascotas;
    }
}
