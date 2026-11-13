
package com.udh.apphuellitas;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnIngresar;
    private EditText edUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        edUsuario = findViewById(R.id.edUsuario);
        btnIngresar = findViewById(R.id.btnIngresar);

        btnIngresar.setOnClickListener(v -> {

            // Obtener el usuario escrito
            String usuario = edUsuario.getText()
                    .toString().trim();

            // Validar que no esté vacío
            if (usuario.isEmpty()) {
                edUsuario.setError("Ingrese su usuario");
                edUsuario.requestFocus();
                return;
            }


            getSharedPreferences("HuellitasPrefs", MODE_PRIVATE)
                    .edit()
                    .putString("USUARIO", usuario)
                    .apply();


            // Abrir Inicio y enviar el usuario
            Intent intent = new Intent(
                    MainActivity.this,
                    InicioPantalla.class
            );

            intent.putExtra("USUARIO", usuario);

            startActivity(intent);
        });
    }
}
