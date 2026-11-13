
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

public class Registro extends MenuActivity {

    // =====================================
    // 1. DATOS DE LA MASCOTA
    // =====================================

    private EditText etNombreMascota;
    private EditText etRaza;
    private Spinner spEspecie;

    private Button btnFechaNacimiento;
    private Button btnGuardarMascota;
    private Button btnCancelarRegistro;

    private TextView txtEstadoRegistro;

    private String fechaNacimiento = "";
    private String idMascotaActual = "";

    // =====================================
    // 2. DATOS DE VACUNAS
    // =====================================

    private EditText etNombreVacuna;
    private EditText etLoteVacuna;

    private Button btnFechaVacuna;
    private Button btnGuardarVacuna;

    private String fechaVacuna = "";

    // =====================================
    // 3. DATOS DE DIETAS
    // =====================================

    private EditText etMarcaAlimento;
    private EditText etRacionGramos;
    private EditText etFrecuenciaDiaria;

    private Button btnGuardarDieta;

    // =====================================
    // 4. VISITAS VETERINARIAS
    // =====================================

    private EditText etLugarVeterinaria;
    private EditText etMotivoVisita;
    private EditText etDiagnostico;

    private Button btnFechaVisita;
    private Button btnGuardarVisita;

    private String fechaVisita = "";

    // =====================================
    // 5. INICIAR ACTIVITY
    // =====================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Cargar el contenido dentro del menú
        cargarContenido(R.layout.activity_registro);

        // ---------------------------------
        // Componentes de mascota
        // ---------------------------------

        etNombreMascota =
                findViewById(R.id.etNombreMascota);

        etRaza = findViewById(R.id.etRaza);
        spEspecie = findViewById(R.id.spEspecie);

        btnFechaNacimiento =
                findViewById(R.id.btnFechaNacimiento);

        btnGuardarMascota =
                findViewById(R.id.btnGuardarMascota);

        btnCancelarRegistro =
                findViewById(R.id.btnCancelarRegistro);

        txtEstadoRegistro =
                findViewById(R.id.txtEstadoRegistro);

        // ---------------------------------
        // Componentes de vacunas
        // ---------------------------------

        etNombreVacuna =
                findViewById(R.id.etNombreVacuna);

        etLoteVacuna =
                findViewById(R.id.etLoteVacuna);

        btnFechaVacuna =
                findViewById(R.id.btnFechaVacuna);

        btnGuardarVacuna =
                findViewById(R.id.btnGuardarVacuna);

        // ---------------------------------
        // Componentes de dietas
        // ---------------------------------

        etMarcaAlimento =
                findViewById(R.id.etMarcaAlimento);

        etRacionGramos =
                findViewById(R.id.etRacionGramos);

        etFrecuenciaDiaria =
                findViewById(R.id.etFrecuenciaDiaria);

        btnGuardarDieta =
                findViewById(R.id.btnGuardarDieta);

        // ---------------------------------
        // Componentes de visitas
        // ---------------------------------

        etLugarVeterinaria =
                findViewById(R.id.etLugarVeterinaria);

        etMotivoVisita =
                findViewById(R.id.etMotivoVisita);

        etDiagnostico =
                findViewById(R.id.etDiagnostico);

        btnFechaVisita =
                findViewById(R.id.btnFechaVisita);

        btnGuardarVisita =
                findViewById(R.id.btnGuardarVisita);

        // Configurar especies
        configurarEspecies();

        // ---------------------------------
        // Recuperar estado temporal
        // ---------------------------------

        if (savedInstanceState != null) {

            fechaNacimiento = savedInstanceState
                    .getString("FECHA_NACIMIENTO", "");

            fechaVacuna = savedInstanceState
                    .getString("FECHA_VACUNA", "");

            fechaVisita = savedInstanceState
                    .getString("FECHA_VISITA", "");

            idMascotaActual = savedInstanceState
                    .getString("MASCOTA_ACTUAL", "");

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
                        "Mascota guardada. " +
                                "Puedes agregar sus cuidados."
                );

                // Evitar registrar accidentalmente
                // otra mascota como la misma sesión
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

        btnGuardarMascota.setOnClickListener(
                v -> guardarMascota()
        );

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

        String nombre = etNombreMascota
                .getText().toString().trim();

        String raza = etRaza
                .getText().toString().trim();

        int posicion = spEspecie
                .getSelectedItemPosition();

        // Validar nombre
        if (nombre.isEmpty()) {

            etNombreMascota.setError(
                    "Ingrese el nombre de la mascota"
            );

            etNombreMascota.requestFocus();
            return;
        }

        // Validar especie
        if (posicion == 0) {

            mostrarMensaje(
                    "Seleccione una especie"
            );

            return;
        }

        // Validar raza
        if (raza.isEmpty()) {

            etRaza.setError(
                    "Ingrese la raza o mestizo"
            );

            etRaza.requestFocus();
            return;
        }

        // Validar nacimiento
        if (fechaNacimiento.isEmpty()) {

            mostrarMensaje(
                    "Seleccione la fecha de nacimiento"
            );

            return;
        }

        String especie = spEspecie
                .getSelectedItem().toString();

        SharedPreferences prefs =
                getSharedPreferences(
                        "HuellitasPrefs",
                        MODE_PRIVATE
                );

        // Usuario de la simulación
        String usuario = prefs.getString(
                "USUARIO",
                ""
        );

        if (usuario.isEmpty()) {

            mostrarMensaje(
                    "Primero debe ingresar un usuario"
            );

            return;
        }

        try {

            String nuevoId =
                    UUID.randomUUID().toString();

            JSONObject mascota =
                    new JSONObject();

            mascota.put("idMascota", nuevoId);
            mascota.put("idUsuario", usuario);
            mascota.put("nombre", nombre);
            mascota.put("especie", especie);
            mascota.put("raza", raza);

            mascota.put(
                    "fechaNacimiento",
                    fechaNacimiento
            );

            // Recuperar mascotas registradas
            String datos = prefs.getString(
                    "MASCOTAS",
                    "[]"
            );

            JSONArray mascotas =
                    new JSONArray(datos);

            mascotas.put(mascota);

            boolean guardado = prefs.edit()
                    .putString(
                            "MASCOTAS",
                            mascotas.toString()
                    )
                    .commit();

            if (!guardado) {

                txtEstadoRegistro.setText(
                        "No se pudo guardar la mascota"
                );

                return;
            }

            // Asociar los futuros cuidados
            // a esta mascota guardada
            idMascotaActual = nuevoId;

            actualizarBotonesCuidados();

            // Evitar duplicados por pulsaciones
            bloquearFormularioMascota();

            txtEstadoRegistro.setText(
                    "Mascota registrada: " + nombre +
                            ". Ahora puedes registrar sus cuidados."
            );

            mostrarMensaje(
                    "Mascota guardada correctamente"
            );

        } catch (JSONException e) {

            txtEstadoRegistro.setText(
                    "Error al procesar la mascota"
            );
        }
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
