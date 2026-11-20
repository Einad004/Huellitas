
package com.udh.apphuellitas;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.udh.apphuellitas.model.AppDatabase;
import com.udh.apphuellitas.model.Dieta;
import com.udh.apphuellitas.model.Mascota;
import com.udh.apphuellitas.model.Vacuna;
import com.udh.apphuellitas.model.VisitaVeterinaria;

import java.util.ArrayList;
import java.util.List;
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

    // VACUNA
    private Spinner spVacunas;
    private TextView txtDetalleVacuna;
    private Button btnEliminarVacuna;
    private List<Vacuna> vacunas = new ArrayList<>();
    private Vacuna vacunaSeleccionada;

    private LinearLayout contenedorDietas;
    private TextView txtDetalleDieta;
    private Button btnEliminarDieta;
    private List<Dieta> dietas = new ArrayList<>();
    private Dieta dietaSeleccionada;

    private LinearLayout contenedorVisitas;
    private TextView txtDetalleVisita;
    private Button btnEliminarVisita;
    private List<VisitaVeterinaria> visitas = new ArrayList<>();
    private VisitaVeterinaria visitaSeleccionada;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cargarContenido(R.layout.activity_buscar_mascota);

        etBuscarId = findViewById(R.id.etBuscarId);
        txtResultado = findViewById(R.id.txtResultado);
        btnBuscarId = findViewById(R.id.btnBuscarId);
        btnEditarMascota = findViewById(R.id.btnEditarMascota);
        btnEliminarMascota = findViewById(R.id.btnEliminarMascota);

        spVacunas = findViewById(R.id.spVacunas);
        txtDetalleVacuna = findViewById(R.id.txtDetalleVacuna);
        btnEliminarVacuna = findViewById(R.id.btnEliminarVacuna);

        contenedorDietas = findViewById(R.id.contenedorDietas);
        txtDetalleDieta = findViewById(R.id.txtDetalleDieta);
        btnEliminarDieta = findViewById(R.id.btnEliminarDieta);

        contenedorVisitas = findViewById(R.id.contenedorVisitas);
        txtDetalleVisita = findViewById(R.id.txtDetalleVisita);
        btnEliminarVisita = findViewById(R.id.btnEliminarVisita);

        db = AppDatabase.getInstance(this);
        io = Executors.newSingleThreadExecutor();

        btnBuscarId.setOnClickListener(v -> buscarMascota());

        btnEditarMascota.setOnClickListener(v -> {

            if (mascotaEncontrada == null) {
                return;
            }
            Intent intent = new Intent(
                    BuscarMascota.this,
                    Registro.class
            );
            intent.putExtra(
                    "ID_MASCOTA",
                    mascotaEncontrada.getIdMascota()
            );

            // Vacuna seleccionada o primera disponible
            Vacuna vacuna = vacunaSeleccionada;
            if (vacuna == null && !vacunas.isEmpty()) {
                vacuna = vacunas.get(0);
            }
            if (vacuna != null) {
                intent.putExtra(
                        "ID_VACUNA",
                        vacuna.getIdVacuna()
                );
            }

            // Dieta seleccionada o primera disponible
            Dieta dieta = dietaSeleccionada;
            if (dieta == null && !dietas.isEmpty()) {
                dieta = dietas.get(0);
            }
            if (dieta != null) {
                intent.putExtra(
                        "ID_DIETA",
                        dieta.getIdDieta()
                );
            }


            // Visita seleccionada o primera disponible
            VisitaVeterinaria visita = visitaSeleccionada;
            if (visita == null && !visitas.isEmpty()) {
                visita = visitas.get(0);
            }
            if (visita != null) {
                intent.putExtra(
                        "ID_VISITA",
                        visita.getIdVisita()
                );
                android.util.Log.d(
                        "HUELLITAS",
                        "Enviando ID_VISITA: " +
                                visita.getIdVisita()
                );
            } else {
                android.util.Log.d(
                        "HUELLITAS",
                        "Esta mascota no tiene visitas cargadas"
                );
            }
            startActivity(intent);
        });


        btnEliminarMascota.setOnClickListener(v -> {

            if (mascotaEncontrada != null) {
                confirmarEliminacion();
            }
        });

        // VACUNA
        spVacunas.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(
                            AdapterView<?> parent,
                            View view,
                            int position,
                            long id) {

                        if (position < vacunas.size()) {
                            vacunaSeleccionada = vacunas.get(position);

                            txtDetalleVacuna.setText(
                                    "Vacuna: " + vacunaSeleccionada.getNombreVacuna()
                                            + "\nLote: " + vacunaSeleccionada.getLote()
                                            + "\nFecha: " + vacunaSeleccionada.getFechaAplicacion()
                            );

                            btnEliminarVacuna.setEnabled(true);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });


        btnEliminarVacuna.setOnClickListener(v -> {

            if (vacunaSeleccionada == null ||
                    mascotaEncontrada == null) {
                return;
            }

            new AlertDialog.Builder(this)
                    .setTitle("Eliminar vacuna")
                    .setMessage("¿Deseas eliminar esta vacuna?")
                    .setPositiveButton(
                            "Eliminar",
                            (dialog, which) -> eliminarVacuna()
                    )
                    .setNegativeButton("Cancelar", null)
                    .show();
        });



        btnEliminarDieta.setOnClickListener(v -> {

            if (dietaSeleccionada == null ||
                    mascotaEncontrada == null) {
                return;
            }

            new AlertDialog.Builder(this)
                    .setTitle("Eliminar alimentación")
                    .setMessage(
                            "¿Deseas eliminar esta alimentación?"
                    )
                    .setPositiveButton(
                            "Eliminar",
                            (dialog, which) -> eliminarDieta()
                    )
                    .setNegativeButton("Cancelar", null)
                    .show();
        });

        btnEliminarVisita.setOnClickListener(v -> {

            if (visitaSeleccionada == null ||
                    mascotaEncontrada == null) {
                return;
            }

            new AlertDialog.Builder(this)
                    .setTitle("Eliminar visita")
                    .setMessage(
                            "¿Deseas eliminar esta visita veterinaria?"
                    )
                    .setPositiveButton(
                            "Eliminar",
                            (dialog, which) -> eliminarVisita()
                    )
                    .setNegativeButton("Cancelar", null)
                    .show();
        });



    }

    // BUSCAR MASCOTA POR ID
    private void buscarMascota() {

        String id = etBuscarId.getText()
                .toString().trim();

        mascotaEncontrada = null;

        visitaSeleccionada = null;
        visitas = new ArrayList<>();

        btnEliminarVisita.setEnabled(false);

        contenedorVisitas.removeAllViews();

        txtDetalleVisita.setText(
                "Busca una mascota para consultar sus visitas."
        );

        btnEditarMascota.setVisibility(View.GONE);
        btnEliminarMascota.setVisibility(View.GONE);

        if (id.isEmpty()) {
            etBuscarId.setError("Ingrese un ID");
            return;
        }

        txtResultado.setText("Buscando...");

        io.execute(() -> {

            try {

                Mascota mascota = db.mascotaDao().buscarPorId(id);

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

                        listarVacunas(mascota.getIdMascota());
                        listarDietas(mascota.getIdMascota());
                        listarVisitas(mascota.getIdMascota());

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

    // CONFIRMAR ELIMINACIÓN
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

    // ELIMINAR DE ROOM
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

    // VACUNA
    private void listarVacunas(String idMascota) {

        vacunaSeleccionada = null;
        btnEliminarVacuna.setEnabled(false);

        io.execute(() -> {

            try {
                List<Vacuna> lista = db.vacunaDao()
                        .listarPorMascota(idMascota);

                runOnUiThread(() -> {

                    vacunas = lista;

                    List<String> nombres = new ArrayList<>();

                    for (Vacuna vacuna : vacunas) {
                        nombres.add(vacuna.getNombreVacuna());
                    }

                    ArrayAdapter<String> adapter =
                            new ArrayAdapter<>(
                                    this,
                                    android.R.layout.simple_spinner_item,
                                    nombres
                            );

                    adapter.setDropDownViewResource(
                            android.R.layout.simple_spinner_dropdown_item
                    );

                    spVacunas.setAdapter(adapter);

                    if (vacunas.isEmpty()) {
                        txtDetalleVacuna.setText(
                                "Esta mascota no tiene vacunas registradas."
                        );
                    }
                });

            } catch (Exception e) {

                runOnUiThread(() ->
                        txtDetalleVacuna.setText(
                                "Error al consultar las vacunas"
                        )
                );
            }
        });
    }

    // Eliminar Vacuna

    private void eliminarVacuna() {

        if (vacunaSeleccionada == null ||
                mascotaEncontrada == null) {
            return;
        }

        Vacuna vacuna = vacunaSeleccionada;

        String idMascota = mascotaEncontrada.getIdMascota();
        btnEliminarVacuna.setEnabled(false);

        io.execute(() -> {
            try {
                int filas = db.vacunaDao().eliminar(vacuna);
                runOnUiThread(() -> {

                    if (filas > 0) {

                        vacunaSeleccionada = null;
                        txtDetalleVacuna.setText(
                                "Vacuna eliminada correctamente"
                        );
                        listarVacunas(idMascota);
                    } else {
                        txtDetalleVacuna.setText(
                                "No se encontró la vacuna"
                        );
                        btnEliminarVacuna.setEnabled(true);
                    }
                });
            } catch (Exception e) {
                android.util.Log.e(
                        "HUELLITAS",
                        "Error al eliminar vacuna",
                        e
                );
                runOnUiThread(() -> {
                    txtDetalleVacuna.setText(
                            "Error al eliminar vacuna"
                    );
                    btnEliminarVacuna.setEnabled(true);
                });
            }
        });
    }


    // DIETAS
    private void listarDietas(String idMascota) {

        dietaSeleccionada = null;
        dietas.clear();
        btnEliminarDieta.setEnabled(false);

        contenedorDietas.removeAllViews();
        txtDetalleDieta.setText("Cargando alimentación...");

        io.execute(() -> {

            try {

                List<Dieta> lista = db.dietaDao()
                        .listarPorMascota(idMascota);

                runOnUiThread(() -> {

                    // Evitar mostrar dietas de otra búsqueda
                    if (mascotaEncontrada == null ||
                            !idMascota.equals(
                                    mascotaEncontrada.getIdMascota())) {
                        return;
                    }

                    dietas = lista;
                    contenedorDietas.removeAllViews();

                    if (dietas.isEmpty()) {

                        txtDetalleDieta.setText(
                                "Esta mascota no tiene alimentación registrada."
                        );
                        return;
                    }

                    txtDetalleDieta.setText(
                            "Selecciona una alimentación para ver sus datos."
                    );

                    for (Dieta dieta : dietas) {

                        TextView item = new TextView(this);

                        item.setText(
                                dieta.getMarcaAlimento() +
                                        " | " + dieta.getRacionGramos() +
                                        " g | " + dieta.getFrecuenciaDiaria() +
                                        " veces al día"
                        );

                        item.setTextSize(16);
                        item.setPadding(12, 20, 12, 20);

                        item.setOnClickListener(v -> {

                            dietaSeleccionada = dieta;

                            txtDetalleDieta.setText(
                                    "Alimentación: " +
                                            dieta.getMarcaAlimento() +
                                            "\nRación: " +
                                            dieta.getRacionGramos() + " g" +
                                            "\nFrecuencia: " +
                                            dieta.getFrecuenciaDiaria() +
                                            " veces al día"
                            );

                            btnEliminarDieta.setEnabled(true);
                        });

                        contenedorDietas.addView(item);
                    }
                });

            } catch (Exception e) {

                android.util.Log.e(
                        "HUELLITAS",
                        "Error al listar dietas",
                        e
                );

                runOnUiThread(() ->
                        txtDetalleDieta.setText(
                                "Error al consultar alimentación"
                        )
                );
            }
        });
    }

    // Eliminar Dieta
    private void eliminarDieta() {

        if (dietaSeleccionada == null ||
                mascotaEncontrada == null) {
            return;
        }

        Dieta dieta = dietaSeleccionada;
        String idMascota = mascotaEncontrada.getIdMascota();

        btnEliminarDieta.setEnabled(false);

        io.execute(() -> {

            try {

                int filas = db.dietaDao().eliminar(dieta);

                runOnUiThread(() -> {

                    if (filas > 0) {

                        dietaSeleccionada = null;

                        txtDetalleDieta.setText(
                                "Alimentación eliminada correctamente"
                        );

                        listarDietas(idMascota);

                    } else {

                        txtDetalleDieta.setText(
                                "No se encontró la alimentación"
                        );

                        btnEliminarDieta.setEnabled(true);
                    }
                });

            } catch (Exception e) {

                android.util.Log.e(
                        "HUELLITAS",
                        "Error al eliminar dieta",
                        e
                );

                runOnUiThread(() -> {

                    txtDetalleDieta.setText(
                            "Error al eliminar alimentación"
                    );

                    btnEliminarDieta.setEnabled(true);
                });
            }
        });
    }

    // VISITAS VETERINARIAS
    // Listar Visitas
    private void listarVisitas(String idMascota) {

        visitaSeleccionada = null;
        btnEliminarVisita.setEnabled(false);

        contenedorVisitas.removeAllViews();

        txtDetalleVisita.setText(
                "Cargando visitas veterinarias..."
        );

        io.execute(() -> {

            try {

                List<VisitaVeterinaria> lista =
                        db.visitaVeterinariaDao()
                                .listarPorMascota(idMascota);

                android.util.Log.d(
                        "HUELLITAS",
                        "Visitas encontradas: " + lista.size()
                );

                runOnUiThread(() -> {

                    if (mascotaEncontrada == null ||
                            !idMascota.equals(
                                    mascotaEncontrada.getIdMascota())) {
                        return;
                    }

                    visitas = lista;
                    contenedorVisitas.removeAllViews();

                    if (visitas.isEmpty()) {

                        txtDetalleVisita.setText(
                                "Esta mascota no tiene visitas registradas."
                        );
                        return;
                    }

                    txtDetalleVisita.setText(
                            "Selecciona una visita para ver sus datos."
                    );

                    for (VisitaVeterinaria visita : visitas) {

                        TextView item = new TextView(this);

                        item.setText(
                                "Fecha: " + visita.getFecha() +
                                        "\nLugar: " + visita.getLugar() +
                                        "\nMotivo: " + visita.getMotivo()
                        );

                        item.setTextSize(16);
                        item.setPadding(20, 24, 20, 24);

                        item.setOnClickListener(v -> {

                            visitaSeleccionada = visita;

                            txtDetalleVisita.setText(
                                    "Fecha: " + visita.getFecha() +
                                            "\nLugar: " + visita.getLugar() +
                                            "\nMotivo: " + visita.getMotivo() +
                                            "\nDiagnóstico: " +
                                            visita.getDiagnostico()
                            );

                            btnEliminarVisita.setEnabled(true);
                        });

                        contenedorVisitas.addView(item);
                    }
                });

            } catch (Exception e) {

                android.util.Log.e(
                        "HUELLITAS",
                        "Error al listar visitas",
                        e
                );

                runOnUiThread(() ->
                        txtDetalleVisita.setText(
                                "Error al consultar visitas"
                        )
                );
            }
        });
    }

    // Eliminar Visita

    private void eliminarVisita() {

        if (visitaSeleccionada == null ||
                mascotaEncontrada == null) {
            return;
        }

        VisitaVeterinaria visita = visitaSeleccionada;

        String idMascota =
                mascotaEncontrada.getIdMascota();

        btnEliminarVisita.setEnabled(false);

        io.execute(() -> {

            try {

                int filas = db.visitaVeterinariaDao()
                        .eliminar(visita);

                runOnUiThread(() -> {

                    if (filas > 0) {

                        visitaSeleccionada = null;

                        listarVisitas(idMascota);

                    } else {

                        txtDetalleVisita.setText(
                                "No se encontró la visita"
                        );

                        btnEliminarVisita.setEnabled(true);
                    }
                });

            } catch (Exception e) {

                android.util.Log.e(
                        "HUELLITAS",
                        "Error al eliminar visita",
                        e
                );

                runOnUiThread(() -> {

                    txtDetalleVisita.setText(
                            "Error al eliminar visita"
                    );

                    btnEliminarVisita.setEnabled(true);
                });
            }
        });
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
