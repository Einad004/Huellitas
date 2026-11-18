
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
import com.udh.apphuellitas.model.Mascota;

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

        // ---------------------------------
        // Recuperar estado temporal
        // ---------------------------------



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

            if (!idMascotaActual.isEmpty()) {
                txtEstadoRegistro.setText(
                        "Mascota guardada. Puedes agregar sus cuidados."
                );
                bloquearFormularioMascota();
            }
        }

        // Habilitar cuidados solo con mascota
        actualizarBotonesCuidados();

        // ---------------------------------
        // Eventos del formulario
        // ---------------------------------

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

        btnGuardarVacuna.setOnClickListener(
                v -> guardarVacuna()
        );

        btnGuardarDieta.setOnClickListener(
                v -> guardarDieta()
        );

        btnFechaVisita.setOnClickListener(
                v -> seleccionarFechaVisita()
        );

        btnGuardarVisita.setOnClickListener(
                v -> guardarVisita()
        );
    }

    // =====================================
    // 6. CONFIGURAR ESPECIES
    // =====================================

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

    // =====================================
    // 7. SELECCIÓN DE FECHAS
    // =====================================

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

    // =====================================
    // 8. GUARDAR MASCOTA
    // =====================================
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
                runOnUiThread(() ->
                        txtEstadoRegistro.setText("Error al guardar mascota")
                );
            }
        });
    }

    // =====================================
    // 9. CARGAR DATOS
    // =====================================
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
                    btnGuardarMascota.setText(
                            "Actualizar mascota"
                    );

                    btnGuardarMascota.setEnabled(true);

                    actualizarBotonesCuidados();

                    txtEstadoRegistro.setText(
                            "Editando: " + mascota.getNombre()
                    );
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

    // =====================================
    // ACTUALIZAR MASCOTA
    // =====================================
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

        // Modificar los datos del objeto
        mascotaEditar.setNombre(nombre);
        mascotaEditar.setRaza(raza);

        mascotaEditar.setEspecie(
                spEspecie.getSelectedItem().toString()
        );

        mascotaEditar.setFechaNacimiento(
                fechaNacimiento
        );

        btnGuardarMascota.setEnabled(false);

        // Actualizar en Room
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


    // =====================================
    // 9. GUARDAR VACUNA
    // =====================================

    private void guardarVacuna() {

        if (!existeMascotaActual()) {
            return;
        }

        String nombre = etNombreVacuna
                .getText().toString().trim();

        String lote = etLoteVacuna
                .getText().toString().trim();

        if (nombre.isEmpty()) {

            etNombreVacuna.setError(
                    "Ingrese el nombre de la vacuna"
            );

            etNombreVacuna.requestFocus();
            return;
        }

        if (lote.isEmpty()) {

            etLoteVacuna.setError(
                    "Ingrese el número de lote"
            );

            etLoteVacuna.requestFocus();
            return;
        }

        if (fechaVacuna.isEmpty()) {

            mostrarMensaje(
                    "Seleccione la fecha de aplicación"
            );

            return;
        }

        try {

            JSONObject vacuna = new JSONObject();

            vacuna.put(
                    "idVacuna",
                    UUID.randomUUID().toString()
            );

            vacuna.put(
                    "idMascota",
                    idMascotaActual
            );

            vacuna.put("nombreVacuna", nombre);
            vacuna.put("lote", lote);

            vacuna.put(
                    "fechaAplicacion",
                    fechaVacuna
            );

            if (guardarDato("VACUNAS", vacuna)) {

                mostrarMensaje(
                        "Vacuna guardada correctamente"
                );

                etNombreVacuna.setText("");
                etLoteVacuna.setText("");

                fechaVacuna = "";

                btnFechaVacuna.setText(
                        "Fecha de aplicación"
                );

            } else {

                mostrarErrorGuardado();
            }

        } catch (JSONException e) {

            mostrarErrorGuardado();
        }
    }

    // =====================================
    // 10. GUARDAR DIETA
    // =====================================

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

        if (marca.isEmpty()) {

            etMarcaAlimento.setError(
                    "Ingrese la marca o tipo de alimento"
            );

            etMarcaAlimento.requestFocus();
            return;
        }

        double gramos;
        int comidas;

        try {

            gramos = Double.parseDouble(racion);
            comidas = Integer.parseInt(frecuencia);

        } catch (NumberFormatException e) {

            mostrarMensaje(
                    "Ingrese cantidades válidas"
            );

            return;
        }

        if (!Double.isFinite(gramos)
                || gramos <= 0 || comidas <= 0) {

            mostrarMensaje(
                    "Ingrese cantidades mayores a cero"
            );

            return;
        }

        try {

            JSONObject dieta = new JSONObject();

            dieta.put(
                    "idDieta",
                    UUID.randomUUID().toString()
            );

            dieta.put(
                    "idMascota",
                    idMascotaActual
            );

            dieta.put(
                    "marcaAlimento",
                    marca
            );

            dieta.put(
                    "racionGramos",
                    gramos
            );

            dieta.put(
                    "frecuenciaDiaria",
                    comidas
            );

            if (guardarDato("DIETAS", dieta)) {

                mostrarMensaje(
                        "Dieta guardada correctamente"
                );

                etMarcaAlimento.setText("");
                etRacionGramos.setText("");
                etFrecuenciaDiaria.setText("");

            } else {

                mostrarErrorGuardado();
            }

        } catch (JSONException e) {

            mostrarErrorGuardado();
        }
    }

    // =====================================
    // 11. GUARDAR VISITA VETERINARIA
    // =====================================

    private void guardarVisita() {

        if (!existeMascotaActual()) {
            return;
        }

        String lugar = etLugarVeterinaria
                .getText().toString().trim();

        String motivo = etMotivoVisita
                .getText().toString().trim();

        String diagnostico = etDiagnostico
                .getText().toString().trim();

        if (fechaVisita.isEmpty()) {

            mostrarMensaje(
                    "Seleccione la fecha de visita"
            );

            return;
        }

        if (lugar.isEmpty()) {

            etLugarVeterinaria.setError(
                    "Ingrese el lugar de la visita"
            );

            etLugarVeterinaria.requestFocus();
            return;
        }

        if (motivo.isEmpty()) {

            etMotivoVisita.setError(
                    "Ingrese el motivo de consulta"
            );

            etMotivoVisita.requestFocus();
            return;
        }

        try {

            JSONObject visita = new JSONObject();

            visita.put(
                    "idVisita",
                    UUID.randomUUID().toString()
            );

            visita.put(
                    "idMascota",
                    idMascotaActual
            );

            visita.put("fecha", fechaVisita);
            visita.put("lugar", lugar);
            visita.put("motivo", motivo);

            visita.put(
                    "diagnostico",
                    diagnostico
            );

            if (guardarDato("VISITAS", visita)) {

                mostrarMensaje(
                        "Visita veterinaria guardada"
                );

                fechaVisita = "";

                btnFechaVisita.setText(
                        "Fecha de visita"
                );

                etLugarVeterinaria.setText("");
                etMotivoVisita.setText("");
                etDiagnostico.setText("");

            } else {

                mostrarErrorGuardado();
            }

        } catch (JSONException e) {

            mostrarErrorGuardado();
        }
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

        boolean habilitado =
                !idMascotaActual.isEmpty();

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

        Toast.makeText(
                this,
                mensaje,
                Toast.LENGTH_SHORT
        ).show();
    }

    private void mostrarErrorGuardado() {

        mostrarMensaje(
                "No se pudo guardar el registro"
        );
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
