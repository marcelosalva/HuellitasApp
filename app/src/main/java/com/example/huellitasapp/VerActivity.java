package com.example.huellitasapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;

public class VerActivity extends AppCompatActivity {
    private String id;
    private FirebaseFirestore db;
    private TextView txtNombre, txtEspecie, txtEdad, txtVacunado, txtEstado, txtDescripcion;
    private Button btnEditar, btnEliminar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver);
        txtNombre = findViewById(R.id.detalle_nombre);
        txtEspecie = findViewById(R.id.detalle_especie);
        txtEdad = findViewById(R.id.detalle_edad);
        txtVacunado = findViewById(R.id.detalle_vacunado);
        txtEstado = findViewById(R.id.detalle_estado);
        txtDescripcion = findViewById(R.id.detalle_descripcion);
        btnEditar = findViewById(R.id.btn_editar);
        btnEliminar = findViewById(R.id.btn_eliminar);
        id = getIntent().getStringExtra(ListarActivity.EXTRA_ID);
        if (id == null || id.trim().isEmpty() || id.contains("/")) {
            Toast.makeText(this, "ID de mascota inválido", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        db = FirebaseFirestore.getInstance();
        btnEditar.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditarActivity.class);
            intent.putExtra(ListarActivity.EXTRA_ID, id);
            startActivity(intent);
        });
        btnEliminar.setOnClickListener(v -> confirmarEliminacion());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (db != null) { cargarMascota(); }
    }

    private void cargarMascota() {
        btnEditar.setEnabled(false);
        btnEliminar.setEnabled(false);
        db.collection("mascotas").document(id).get()
                .addOnSuccessListener(doc -> {
                    if (!doc.exists()) {
                        Toast.makeText(this, "La mascota ya no existe", Toast.LENGTH_SHORT).show();
                        volverAlListado();
                        return;
                    }
                    Mascota mascota = doc.toObject(Mascota.class);
                    if (mascota != null) {
                        mostrarMascota(mascota);
                        btnEditar.setEnabled(true);
                        btnEliminar.setEnabled(true);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al cargar: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    finish();
                });
    }

    private void mostrarMascota(Mascota mascota) {
        txtNombre.setText(mascota.getNombre());
        txtEspecie.setText(mascota.getEspecie());
        txtEdad.setText(mascota.getEdad() + " años");
        txtVacunado.setText(mascota.getVacunado() ? "Sí" : "No");
        txtEstado.setText(mascota.getEstado());
        txtDescripcion.setText(mascota.getDescripcion());
    }

    private void confirmarEliminacion() {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar mascota")
                .setMessage("¿Seguro que deseas eliminar esta mascota?")
                .setPositiveButton("Sí, eliminar", (dialog, which) -> eliminar())
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void eliminar() {
        btnEliminar.setEnabled(false);
        btnEditar.setEnabled(false);
        db.collection("mascotas").document(id).delete()
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Mascota eliminada", Toast.LENGTH_SHORT).show();
                    volverAlListado();
                })
                .addOnFailureListener(e -> {
                    btnEliminar.setEnabled(true);
                    btnEditar.setEnabled(true);
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
