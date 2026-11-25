package com.example.huellitasapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;

public class AgregarActivity extends AppCompatActivity {
    private EditText inputNombre, inputEdad, inputDescripcion;
    private Spinner spinnerEspecie, spinnerEstado;
    private CheckBox checkVacunado;
    private Button btnGuardar;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar);
        inputNombre = findViewById(R.id.input_nombre);
        inputEdad = findViewById(R.id.input_edad);
        inputDescripcion = findViewById(R.id.input_descripcion);
        spinnerEspecie = findViewById(R.id.spinner_especie);
        spinnerEstado = findViewById(R.id.spinner_estado);
        checkVacunado = findViewById(R.id.check_vacunado);
        btnGuardar = findViewById(R.id.btn_guardar);
        db = FirebaseFirestore.getInstance();
        btnGuardar.setOnClickListener(v -> guardar());
    }

    // Se valida antes de escribir en Firestore.
    private Mascota leerFormulario() {
        String nombre = inputNombre.getText().toString().trim();
        String textoEdad = inputEdad.getText().toString().trim();
        String descripcion = inputDescripcion.getText().toString().trim();
        if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(textoEdad)
                || TextUtils.isEmpty(descripcion)) {
            Toast.makeText(this, "Completa nombre, edad y descripción", Toast.LENGTH_SHORT).show();
            return null;
        }
        int edad;
        try {
            edad = Integer.parseInt(textoEdad);
            if (edad < 0) {
                inputEdad.setError("La edad no puede ser negativa");
                return null;
            }
        } catch (NumberFormatException e) {
            inputEdad.setError("Ingresa un entero entre 0 y 2147483647");
            return null;
        }
        return new Mascota(nombre, spinnerEspecie.getSelectedItem().toString(), edad,
                checkVacunado.isChecked(), spinnerEstado.getSelectedItem().toString(), descripcion);
    }

    private void guardar() {
        Mascota mascota = leerFormulario();
        if (mascota == null) { return; }
        btnGuardar.setEnabled(false);
        db.collection("mascotas").add(mascota)
                .addOnSuccessListener(resultado -> {
                    Toast.makeText(this, "Mascota guardada", Toast.LENGTH_SHORT).show();
                    volverAlListado();
                })
                .addOnFailureListener(e -> {
                    btnGuardar.setEnabled(true);
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    private void volverAlListado() {
        Intent intent = new Intent(this, ListarActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
}
