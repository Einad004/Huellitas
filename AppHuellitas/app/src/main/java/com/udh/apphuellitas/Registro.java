
package com.udh.apphuellitas;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.udh.apphuellitas.model.AppDatabase;
import com.udh.apphuellitas.model.Dieta;
import com.udh.apphuellitas.model.Mascota;
import com.udh.apphuellitas.model.Vacuna;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Registro extends MenuActivity {

    // 1. DATOS DE LA MASCOTA
    private EditText etNombreMascota;
    private EditText etRaza;
    private Spinner spEspecie;
    private Button btnFechaNacimiento;
    private Button btnGuardarMascota;
    private Button btnCancelarRegistro;
    private TextView txtEstadoRegistro;
    private String fechaNacimiento = "";
    private String idMascotaActual = "";

    // 2. DATOS DE VACUNAS
    private EditText etNombreVacuna;
    private EditText etLoteVacuna;
    private Button btnFechaVacuna;
    private Button btnGuardarVacuna;
    private String fechaVacuna = "";

    // 3. DATOS DE DIETAS
    private EditText etMarcaAlimento;
    private EditText etRacionGramos;
    private EditText etFrecuenciaDiaria;
    private Button btnGuardarDieta;

    // 4. VISITAS VETERINARIAS
    private EditText etLugarVeterinaria;
    private EditText etMotivoVisita;
    private EditText etDiagnostico;
    private Button btnFechaVisita;
    private Button btnGuardarVisita;
    private String fechaVisita = "";
    private AppDatabase db;
    private ExecutorService io;
    private boolean modoEdicion = false;
    private Mascota mascotaEditar;
    private Vacuna vacunaEditar;
    private Dieta dietaEditar;


    // 5. INICIAR ACTIVITY
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Cargar el contenido dentro del menú
        cargarContenido(R.layout.activity_registro);

        etNombreMascota = findViewById(R.id.etNombreMascota);
        etRaza = findViewById(R.id.etRaza);
        spEspecie = findViewById(R.id.spEspecie);
        btnFechaNacimiento = findViewById(R.id.btnFechaNacimiento);
        btnGuardarMascota = findViewById(R.id.btnGuardarMascota);
        btnCancelarRegistro = findViewById(R.id.btnCancelarRegistro);
        txtEstadoRegistro = findViewById(R.id.txtEstadoRegistro);

        // Componentes de vacunas
        etNombreVacuna = findViewById(R.id.etNombreVacuna);
        etLoteVacuna = findViewById(R.id.etLoteVacuna);
        btnFechaVacuna = findViewById(R.id.btnFechaVacuna);
        btnGuardarVacuna = findViewById(R.id.btnGuardarVacuna);

        // Componentes de dietas
        etMarcaAlimento = findViewById(R.id.etMarcaAlimento);
        etRacionGramos = findViewById(R.id.etRacionGramos);
        etFrecuenciaDiaria = findViewById(R.id.etFrecuenciaDiaria);
        btnGuardarDieta = findViewById(R.id.btnGuardarDieta);

        // Componentes de visitas
        etLugarVeterinaria = findViewById(R.id.etLugarVeterinaria);
        etMotivoVisita = findViewById(R.id.etMotivoVisita);
        etDiagnostico = findViewById(R.id.etDiagnostico);
        btnFechaVisita = findViewById(R.id.btnFechaVisita);
        btnGuardarVisita = findViewById(R.id.btnGuardarVisita);

        db = AppDatabase.getInstance(this);
        io = Executors.newSingleThreadExecutor();

        configurarEspecies();

        String idEditar = getIntent().getStringExtra("ID_MASCOTA");

        if (idEditar != null && !idEditar.isEmpty()) {
            modoEdicion = true;
            cargarDatosMascota(idEditar);
        }

        // Recuperar estado temporal
        if (savedInstanceState != null) {

            fechaNacimiento = savedInstanceState.getString("FECHA_NACIMIENTO", "");
            fechaVacuna = savedInstanceState.getString("FECHA_VACUNA", "");
            fechaVisita = savedInstanceState.getString("FECHA_VISITA", "");
            idMascotaActual = savedInstanceState.getString("MASCOTA_ACTUAL", "");

            if (!fechaNacimiento.isEmpty()) {
                btnFechaNacimiento.setText(
                        fechaNacimiento
                );
            }

            if (!fechaVacuna.isEmpty()) {
                btnFechaVacuna.setText(fechaVacuna);
            }

            if (!fechaVisita.isEmpty()) {
                btnFechaVisita.setText(fechaVisita);
            }

            if (!idMascotaActual.isEmpty() && !modoEdicion) {
                txtEstadoRegistro.setText(
                        "Mascota guardada. Puedes agregar sus cuidados."
                );
                bloquearFormularioMascota();
            }
        }

        // Habilitar cuidados solo con mascota
        actualizarBotonesCuidados();

        // Eventos del formulario
        btnFechaNacimiento.setOnClickListener(
                v -> seleccionarFechaNacimiento()
        );


        btnGuardarMascota.setOnClickListener(v -> {
            if (modoEdicion) {
                actualizarMascota();
            } else {
                guardarMascota();
            }
        });


        btnCancelarRegistro.setOnClickListener(
                v -> finish()
        );

        btnFechaVacuna.setOnClickListener(
                v -> seleccionarFechaVacuna()
        );

        btnGuardarVacuna.setOnClickListener(v -> {
            if (vacunaEditar != null) {
                actualizarVacuna();
            } else {
                guardarVacuna();
            }
        });


        btnGuardarDieta.setOnClickListener(v -> {

            if (dietaEditar != null) {
                actualizarDieta();
            } else {
                guardarDieta();
            }
        });


        btnFechaVisita.setOnClickListener(
                v -> seleccionarFechaVisita()
        );

        btnGuardarVisita.setOnClickListener(
                v -> guardarVisita()
        );
    }

    // CONFIGURAR ESPECIES
    private void configurarEspecies() {

        String[] especies = {
                "Selecciona una especie",
                "Perro",
                "Gato",
                "Ave",
                "Conejo",
                "Otro"
        };

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        especies
                );

        adapter.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item
        );

        spEspecie.setAdapter(adapter);
    }

    // SELECCIÓN DE FECHAS
    private void seleccionarFechaNacimiento() {
        mostrarCalendario(1);
    }

    private void seleccionarFechaVacuna() {
        mostrarCalendario(2);
    }

    private void seleccionarFechaVisita() {
        mostrarCalendario(3);
    }

    private void mostrarCalendario(int tipo) {

        Calendar calendario = Calendar.getInstance();

        int anio = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialogo =
                new DatePickerDialog(
                        this,
                        (view, year, month, dayOfMonth) -> {

                            String fecha = String.format(
                                    Locale.US,
                                    "%04d-%02d-%02d",
                                    year,
                                    month + 1,
                                    dayOfMonth
                            );

                            if (tipo == 1) {

                                fechaNacimiento = fecha;

                                btnFechaNacimiento.setText(
                                        fechaNacimiento
                                );

                            } else if (tipo == 2) {

                                fechaVacuna = fecha;

                                btnFechaVacuna.setText(
                                        fechaVacuna
                                );

                            } else if (tipo == 3) {

                                fechaVisita = fecha;

                                btnFechaVisita.setText(
                                        fechaVisita
                                );
                            }
                        },
                        anio,
                        mes,
                        dia
                );

        // Impedir fechas futuras
        dialogo.getDatePicker().setMaxDate(
                System.currentTimeMillis()
        );

        dialogo.show();
    }

    // GUARDAR MASCOTA
    private void guardarMascota() {

        String nombre = etNombreMascota.getText().toString().trim();
        String raza = etRaza.getText().toString().trim();
        String especie = spEspecie.getSelectedItem().toString();

        if (nombre.isEmpty() || raza.isEmpty()
                || spEspecie.getSelectedItemPosition() == 0
                || fechaNacimiento.isEmpty()) {

            Toast.makeText(this,
                    "Complete todos los campos",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        String usuario = getSharedPreferences(
                "HuellitasPrefs", MODE_PRIVATE
        ).getString("USUARIO", "");

        if (usuario.isEmpty()) {
            Toast.makeText(this,
                    "Primero ingrese un usuario",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        String id = UUID.randomUUID().toString();

        Mascota mascota = new Mascota();
        mascota.setIdMascota(id);
        mascota.setIdUsuario(usuario);
        mascota.setNombre(nombre);
        mascota.setRaza(raza);
        mascota.setEspecie(especie);
        mascota.setFechaNacimiento(fechaNacimiento);

        io.execute(() -> {
            try {
                db.mascotaDao().insertar(mascota);

                runOnUiThread(() -> {
                    idMascotaActual = id;
                    actualizarBotonesCuidados();
                    bloquearFormularioMascota();
                    txtEstadoRegistro.setText("Mascota registrada: " + nombre);
                });

            } catch (Exception e) {
                android.util.Log.e(
                        "HUELLITAS",
                        "Error al guardar mascota en Room",
                        e
                );

                runOnUiThread(() ->
                        txtEstadoRegistro.setText(
                                "Error al guardar mascota"
                        )
                );
            }
        });
    }

    // CARGAR DATOS
    private void cargarDatosMascota(String id) {

        btnGuardarMascota.setEnabled(false);

        io.execute(() -> {

            try {
                Mascota mascota =
                        db.mascotaDao().buscarPorId(id);

                runOnUiThread(() -> {

                    String usuario = getSharedPreferences(
                            "HuellitasPrefs",
                            MODE_PRIVATE
                    ).getString("USUARIO", "");

                    if (mascota == null ||
                            !usuario.equals(mascota.getIdUsuario())) {

                        txtEstadoRegistro.setText(
                                "Mascota no encontrada"
                        );
                        return;
                    }

                    // Guardar la mascota encontrada
                    mascotaEditar = mascota;
                    idMascotaActual = mascota.getIdMascota();

                    // Colocar datos en el formulario
                    etNombreMascota.setText(
                            mascota.getNombre()
                    );

                    etRaza.setText(
                            mascota.getRaza()
                    );

                    fechaNacimiento =
                            mascota.getFechaNacimiento();

                    btnFechaNacimiento.setText(
                            fechaNacimiento
                    );

                    // Seleccionar especie
                    for (int i = 0;
                         i < spEspecie.getCount();
                         i++) {

                        if (spEspecie.getItemAtPosition(i)
                                .toString()
                                .equals(mascota.getEspecie())) {

                            spEspecie.setSelection(i);
                            break;
                        }
                    }

                    // Cambiar el texto del botón
                    btnGuardarMascota.setText("Actualizar mascota");
                    btnGuardarMascota.setEnabled(true);

                    actualizarBotonesCuidados();

                    txtEstadoRegistro.setText(
                            "Editando: " + mascota.getNombre()
                    );


                    btnGuardarMascota.setText(
                            "Actualizar mascota"
                    );

                    btnGuardarMascota.setEnabled(true);

                    actualizarBotonesCuidados();

                    txtEstadoRegistro.setText(
                            "Editando: " + mascota.getNombre()
                    );

                    String idVacuna = getIntent().getStringExtra("ID_VACUNA");

                    if (idVacuna != null && !idVacuna.isEmpty()) {
                        cargarVacuna(idVacuna);
                    }

                    String idDieta = getIntent().getStringExtra("ID_DIETA");

                    if (idDieta != null && !idDieta.isEmpty()) {
                        cargarDieta(idDieta);
                    }


                });

            } catch (Exception e) {

                runOnUiThread(() -> {
                    txtEstadoRegistro.setText(
                            "Error al cargar los datos"
                    );
                });
            }
        });
    }

    // Actualizar Mascota
    private void actualizarMascota() {

        if (mascotaEditar == null) {
            return;
        }

        String nombre = etNombreMascota
                .getText().toString().trim();

        String raza = etRaza
                .getText().toString().trim();

        int posicion = spEspecie.getSelectedItemPosition();

        // Validar los campos
        if (nombre.isEmpty() || raza.isEmpty()
                || posicion == 0
                || fechaNacimiento.isEmpty()) {

            mostrarMensaje("Complete todos los campos");
            return;
        }

        mascotaEditar.setNombre(nombre);
        mascotaEditar.setRaza(raza);

        mascotaEditar.setEspecie(
                spEspecie.getSelectedItem().toString()
        );

        mascotaEditar.setFechaNacimiento(
                fechaNacimiento
        );

        btnGuardarMascota.setEnabled(false);

        io.execute(() -> {

            try {
                int filas = db.mascotaDao()
                        .actualizar(mascotaEditar);

                runOnUiThread(() -> {

                    if (filas > 0) {

                        mostrarMensaje(
                                "Mascota actualizada correctamente"
                        );

                        // Regresar a BuscarMascota
                        finish();

                    } else {

                        txtEstadoRegistro.setText(
                                "No se encontró el registro"
                        );

                        btnGuardarMascota.setEnabled(true);
                    }
                });

            } catch (Exception e) {

                runOnUiThread(() -> {

                    mostrarMensaje(
                            "Error al actualizar la mascota"
                    );

                    btnGuardarMascota.setEnabled(true);
                });
            }
        });
    }

    // VACUNA
    // Guardar Vacuna
    private void guardarVacuna() {

        if (!existeMascotaActual()) return;

        String nombre = etNombreVacuna
                .getText().toString().trim();

        String lote = etLoteVacuna
                .getText().toString().trim();

        if (nombre.isEmpty() || lote.isEmpty()
                || fechaVacuna.isEmpty()) {
            mostrarMensaje("Complete los datos de la vacuna");
            return;
        }

        Vacuna vacuna = new Vacuna();
        vacuna.setIdVacuna(UUID.randomUUID().toString());
        vacuna.setIdMascota(idMascotaActual);
        vacuna.setNombreVacuna(nombre);
        vacuna.setLote(lote);
        vacuna.setFechaAplicacion(fechaVacuna);

        btnGuardarVacuna.setEnabled(false);

        io.execute(() -> {
            try {
                db.vacunaDao().insertar(vacuna);

                runOnUiThread(() -> {
                    mostrarMensaje("Vacuna guardada");
                    etNombreVacuna.setText("");
                    etLoteVacuna.setText("");
                    fechaVacuna = "";
                    btnFechaVacuna.setText("Fecha de aplicación");
                    btnGuardarVacuna.setEnabled(true);
                });

            } catch (Exception e) {
                runOnUiThread(() -> {
                    mostrarMensaje("Error al guardar vacuna");
                    btnGuardarVacuna.setEnabled(true);
                });
            }
        });
    }

    // Listar Vacuna
    private void cargarVacuna(String id) {

        io.execute(() -> {

            Vacuna vacuna = db.vacunaDao().buscarPorId(id);

            runOnUiThread(() -> {

                if (vacuna == null) {
                    mostrarMensaje("Vacuna no encontrada");
                    return;
                }

                // Verificar mascota asociada
                if (!vacuna.getIdMascota().equals(
                        idMascotaActual)) {
                    mostrarMensaje("Vacuna no corresponde a la mascota");
                    return;
                }

                vacunaEditar = vacuna;

                etNombreVacuna.setText(
                        vacuna.getNombreVacuna()
                );

                etLoteVacuna.setText(
                        vacuna.getLote()
                );

                fechaVacuna = vacuna.getFechaAplicacion();
                btnFechaVacuna.setText(fechaVacuna);

                btnGuardarVacuna.setEnabled(true);
                btnGuardarVacuna.setText("Actualizar vacuna");
            });
        });
    }

    // Actualizar Vacuna
    private void actualizarVacuna() {

        if (vacunaEditar == null) {
            return;
        }

        String nombre = etNombreVacuna
                .getText().toString().trim();

        String lote = etLoteVacuna
                .getText().toString().trim();

        if (nombre.isEmpty() || lote.isEmpty()
                || fechaVacuna.isEmpty()) {

            mostrarMensaje(
                    "Complete los datos de la vacuna"
            );
            return;
        }

        vacunaEditar.setNombreVacuna(nombre);
        vacunaEditar.setLote(lote);
        vacunaEditar.setFechaAplicacion(fechaVacuna);

        btnGuardarVacuna.setEnabled(false);

        io.execute(() -> {

            try {

                int filas = db.vacunaDao()
                        .actualizar(vacunaEditar);

                runOnUiThread(() -> {

                    if (filas > 0) {

                        mostrarMensaje(
                                "Vacuna actualizada correctamente"
                        );

                        finish();

                    } else {

                        mostrarMensaje(
                                "No se encontró la vacuna"
                        );

                        btnGuardarVacuna.setEnabled(true);
                    }
                });

            } catch (Exception e) {

                runOnUiThread(() -> {

                    mostrarMensaje(
                            "Error al actualizar vacuna"
                    );

                    btnGuardarVacuna.setEnabled(true);
                });
            }
        });
    }

    // DIETA
    // Guardar Dieta
    private void guardarDieta() {

        if (!existeMascotaActual()) {
            return;
        }

        String marca = etMarcaAlimento
                .getText().toString().trim();

        String racion = etRacionGramos
                .getText().toString().trim();

        String frecuencia = etFrecuenciaDiaria
                .getText().toString().trim();

        if (marca.isEmpty() || racion.isEmpty()
                || frecuencia.isEmpty()) {

            mostrarMensaje(
                    "Complete los datos de alimentación"
            );
            return;
        }

        double gramos;
        int comidas;

        try {
            gramos = Double.parseDouble(racion);
            comidas = Integer.parseInt(frecuencia);

        } catch (NumberFormatException e) {

            mostrarMensaje("Ingrese cantidades válidas");
            return;
        }

        if (!Double.isFinite(gramos)
                || gramos <= 0 || comidas <= 0) {

            mostrarMensaje(
                    "Las cantidades deben ser mayores a cero"
            );
            return;
        }

        Dieta dieta = new Dieta();

        dieta.setIdDieta(UUID.randomUUID().toString());
        dieta.setIdMascota(idMascotaActual);
        dieta.setMarcaAlimento(marca);
        dieta.setRacionGramos(gramos);
        dieta.setFrecuenciaDiaria(comidas);

        btnGuardarDieta.setEnabled(false);

        io.execute(() -> {

            try {

                db.dietaDao().insertar(dieta);

                runOnUiThread(() -> {

                    mostrarMensaje(
                            "Alimentación guardada correctamente"
                    );

                    etMarcaAlimento.setText("");
                    etRacionGramos.setText("");
                    etFrecuenciaDiaria.setText("");

                    btnGuardarDieta.setEnabled(true);
                });

            } catch (Exception e) {

                android.util.Log.e(
                        "HUELLITAS",
                        "Error al guardar dieta",
                        e
                );

                runOnUiThread(() -> {

                    mostrarMensaje(
                            "Error al guardar alimentación"
                    );

                    btnGuardarDieta.setEnabled(true);
                });
            }
        });
    }

    // Cargar Dieta

    private void cargarDieta(String id) {

        io.execute(() -> {

            try {

                Dieta dieta = db.dietaDao().buscarPorId(id);

                runOnUiThread(() -> {

                    if (dieta == null) {
                        mostrarMensaje("Alimentación no encontrada");
                        return;
                    }

                    if (!idMascotaActual.equals(
                            dieta.getIdMascota())) {

                        mostrarMensaje(
                                "La alimentación no pertenece a esta mascota"
                        );
                        return;
                    }

                    dietaEditar = dieta;

                    etMarcaAlimento.setText(
                            dieta.getMarcaAlimento()
                    );

                    etRacionGramos.setText(
                            String.valueOf(dieta.getRacionGramos())
                    );

                    etFrecuenciaDiaria.setText(
                            String.valueOf(dieta.getFrecuenciaDiaria())
                    );

                    btnGuardarDieta.setText(
                            "Actualizar alimentación"
                    );

                    btnGuardarDieta.setEnabled(true);
                });

            } catch (Exception e) {

                android.util.Log.e(
                        "HUELLITAS",
                        "Error al cargar dieta",
                        e
                );

                runOnUiThread(() ->
                        mostrarMensaje(
                                "Error al cargar alimentación"
                        )
                );
            }
        });
    }

    // Actualizar Dieta

    private void actualizarDieta() {

        if (dietaEditar == null) {
            return;
        }

        String marca = etMarcaAlimento
                .getText().toString().trim();

        String racion = etRacionGramos
                .getText().toString().trim();

        String frecuencia = etFrecuenciaDiaria
                .getText().toString().trim();

        if (marca.isEmpty() || racion.isEmpty()
                || frecuencia.isEmpty()) {

            mostrarMensaje("Complete los datos");
            return;
        }

        double gramos;
        int comidas;

        try {

            gramos = Double.parseDouble(racion);
            comidas = Integer.parseInt(frecuencia);

        } catch (NumberFormatException e) {

            mostrarMensaje("Ingrese números válidos");
            return;
        }

        if (!Double.isFinite(gramos)
                || gramos <= 0 || comidas <= 0) {

            mostrarMensaje("Ingrese cantidades mayores a cero");
            return;
        }

        dietaEditar.setMarcaAlimento(marca);
        dietaEditar.setRacionGramos(gramos);
        dietaEditar.setFrecuenciaDiaria(comidas);

        btnGuardarDieta.setEnabled(false);

        io.execute(() -> {

            try {

                int filas = db.dietaDao()
                        .actualizar(dietaEditar);

                runOnUiThread(() -> {

                    if (filas > 0) {

                        mostrarMensaje(
                                "Alimentación actualizada correctamente"
                        );

                        finish();

                    } else {

                        mostrarMensaje(
                                "No se encontró la alimentación"
                        );

                        btnGuardarDieta.setEnabled(true);
                    }
                });

            } catch (Exception e) {

                android.util.Log.e(
                        "HUELLITAS",
                        "Error al actualizar dieta",
                        e
                );

                runOnUiThread(() -> {

                    mostrarMensaje(
                            "Error al actualizar alimentación"
                    );

                    btnGuardarDieta.setEnabled(true);
                });
            }
        });
    }


    // =====================================
    // 11. GUARDAR VISITA VETERINARIA
    // =====================================

    private void guardarVisita() {
    }

    // =====================================
    // 12. GUARDADO GENERAL DE JSON
    // =====================================

    private boolean guardarDato(
            String clave,
            JSONObject nuevoRegistro) {

        SharedPreferences prefs =
                getSharedPreferences(
                        "HuellitasPrefs",
                        MODE_PRIVATE
                );

        try {

            String datos = prefs.getString(
                    clave,
                    "[]"
            );

            JSONArray registros =
                    new JSONArray(datos);

            registros.put(nuevoRegistro);

            return prefs.edit()
                    .putString(
                            clave,
                            registros.toString()
                    )
                    .commit();

        } catch (JSONException e) {

            return false;
        }
    }

    // =====================================
    // 13. CONTROL DEL FORMULARIO
    // =====================================

    private boolean existeMascotaActual() {

        if (idMascotaActual.isEmpty()) {

            mostrarMensaje(
                    "Primero registre una mascota"
            );

            return false;
        }

        return true;
    }

    private void actualizarBotonesCuidados() {
        boolean habilitado = !idMascotaActual.isEmpty();

        btnGuardarVacuna.setEnabled(habilitado);
        btnGuardarDieta.setEnabled(habilitado);
        btnGuardarVisita.setEnabled(habilitado);
    }

    private void bloquearFormularioMascota() {
        etNombreMascota.setEnabled(false);
        etRaza.setEnabled(false);
        spEspecie.setEnabled(false);
        btnFechaNacimiento.setEnabled(false);
        btnGuardarMascota.setEnabled(false);
    }

    // =====================================
    // 14. MENSAJES
    // =====================================

    private void mostrarMensaje(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }

    private void mostrarErrorGuardado() {
    }

    // =====================================
    // 15. CONSERVAR ESTADO
    // =====================================

    @Override
    protected void onSaveInstanceState(
            Bundle outState) {

        outState.putString(
                "FECHA_NACIMIENTO",
                fechaNacimiento
        );

        outState.putString(
                "FECHA_VACUNA",
                fechaVacuna
        );

        outState.putString(
                "FECHA_VISITA",
                fechaVisita
        );

        outState.putString(
                "MASCOTA_ACTUAL",
                idMascotaActual
        );

        super.onSaveInstanceState(outState);
    }

    // =====================================
    // 16. NAVEGACIÓN INFERIOR
    // =====================================

    @Override
    protected int obtenerItemActual() {
        return R.id.nav_mascotas;
    }
}
