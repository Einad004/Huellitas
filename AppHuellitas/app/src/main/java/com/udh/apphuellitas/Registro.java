
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

    private EditText etNombreMascota;
    private EditText etRaza;
    private Spinner spEspecie;
    private Button btnFechaNacimiento;
    private Button btnGuardarMascota;
    private Button btnCancelarRegistro;
    private TextView txtEstadoRegistro;
    private String fechaNacimiento = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Cargar dentro del menú compartido
        cargarContenido(R.layout.activity_registro);

        // Vincular los componentes
        etNombreMascota = findViewById(R.id.etNombreMascota);
        etRaza = findViewById(R.id.etRaza);
        spEspecie = findViewById(R.id.spEspecie);
        btnFechaNacimiento = findViewById(R.id.btnFechaNacimiento);
        btnGuardarMascota = findViewById(R.id.btnGuardarMascota);
        btnCancelarRegistro = findViewById(R.id.btnCancelarRegistro);
        txtEstadoRegistro = findViewById(R.id.txtEstadoRegistro);
        configurarEspecies();

        btnFechaNacimiento.setOnClickListener(
                v -> seleccionarFecha()
        );

        btnGuardarMascota.setOnClickListener(
                v -> guardarMascota()
        );

        btnCancelarRegistro.setOnClickListener(
                v -> finish()
        );

        // Recuperar la fecha si se recrea la Activity
        if (savedInstanceState != null) {
            fechaNacimiento = savedInstanceState
                    .getString("FECHA_NACIMIENTO", "");

            if (!fechaNacimiento.isEmpty()) {
                btnFechaNacimiento.setText(
                        fechaNacimiento
                );
            }
        }
    }

    // Configurar las especies disponibles
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
                android.R.layout.simple_spinner_dropdown_item
        );

        spEspecie.setAdapter(adapter);
    }

    // Abrir calendario
    private void seleccionarFecha() {

        Calendar calendario = Calendar.getInstance();

        int anio = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialogo = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {

                    fechaNacimiento = String.format(
                            Locale.US,
                            "%04d-%02d-%02d",
                            year,
                            month + 1,
                            dayOfMonth
                    );

                    btnFechaNacimiento.setText(
                            fechaNacimiento
                    );
                },
                anio,
                mes,
                dia
        );

        // Evitar fechas futuras
        dialogo.getDatePicker().setMaxDate(
                System.currentTimeMillis()
        );

        dialogo.show();
    }

    // Validar y guardar los datos
    private void guardarMascota() {

        String nombre = etNombreMascota.getText().toString().trim();

        String raza = etRaza.getText().toString().trim();
        int posicion = spEspecie.getSelectedItemPosition();

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
            Toast.makeText(
                    this,
                    "Seleccione una especie",
                    Toast.LENGTH_SHORT
            ).show();
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

        // Validar fecha
        if (fechaNacimiento.isEmpty()) {
            Toast.makeText(
                    this,
                    "Seleccione la fecha de nacimiento",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        String especie = spEspecie
                .getSelectedItem().toString();

        // Obtener usuario simulado
        SharedPreferences prefs =
                getSharedPreferences(
                        "HuellitasPrefs",
                        MODE_PRIVATE
                );

        String usuario = prefs.getString(
                "USUARIO",
                ""
        );

        if (usuario.isEmpty()) {
            Toast.makeText(
                    this,
                    "Primero debe ingresar un usuario",
                    Toast.LENGTH_LONG
            ).show();
            return;
        }

        // Crear registro JSON
        try {

            JSONObject mascota = new JSONObject();

            mascota.put(
                    "idMascota",
                    UUID.randomUUID().toString()
            );

            mascota.put("idUsuario", usuario);
            mascota.put("nombre", nombre);
            mascota.put("especie", especie);
            mascota.put("raza", raza);

            mascota.put(
                    "fechaNacimiento",
                    fechaNacimiento
            );

            // Recuperar registros existentes
            String datos = prefs.getString(
                    "MASCOTAS",
                    "[]"
            );

            JSONArray mascotas = new JSONArray(datos);

            // Agregar nueva mascota
            mascotas.put(mascota);

            // Guardar arreglo actualizado
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

            txtEstadoRegistro.setText(
                    "Mascota registrada: " + nombre
            );

            Toast.makeText(
                    this,
                    "Registro guardado correctamente",
                    Toast.LENGTH_SHORT
            ).show();

            limpiarFormulario();

        } catch (JSONException e) {

            txtEstadoRegistro.setText(
                    "Error al procesar el registro"
            );
        }
    }

    // Limpiar después del registro
    private void limpiarFormulario() {

        etNombreMascota.setText("");
        etRaza.setText("");
        spEspecie.setSelection(0);
        fechaNacimiento = "";
        btnFechaNacimiento.setText(
                "Seleccionar fecha"
        );

        etNombreMascota.requestFocus();
    }

    @Override
    protected void onSaveInstanceState(
            Bundle outState
    ) {
        outState.putString(
                "FECHA_NACIMIENTO",
                fechaNacimiento
        );

        super.onSaveInstanceState(outState);
    }

    // Mantener Mascotas seleccionada
    @Override
    protected int obtenerItemActual() {
        return R.id.nav_mascotas;
    }
}
