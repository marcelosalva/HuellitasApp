package com.example.huellitasapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class ListarActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "id_mascota";
    private final List<Mascota> mascotas = new ArrayList<>();
    private MascotaAdapter adapter;
    private TextView textoVacio;
    private FirebaseFirestore db;
    private ListenerRegistration listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar);
        RecyclerView lista = findViewById(R.id.lista_mascotas);
        textoVacio = findViewById(R.id.texto_vacio);
        Button btnAgregar = findViewById(R.id.btn_agregar);
        adapter = new MascotaAdapter(mascotas);
        lista.setLayoutManager(new LinearLayoutManager(this));
        lista.setAdapter(adapter);
        db = FirebaseFirestore.getInstance();
        btnAgregar.setOnClickListener(v -> startActivity(new Intent(this, AgregarActivity.class)));
    }

    @Override
    protected void onStart() {
        super.onStart();
        escucharTiempoReal();
    }

    private void escucharTiempoReal() {
        listener = db.collection("mascotas").addSnapshotListener((snapshots, e) -> {
            if (e != null) {
                textoVacio.setText("No se pudo actualizar el listado. Vuelve a abrir esta pantalla.");
                textoVacio.setVisibility(View.VISIBLE);
                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                return;
            }
            mostrarMascotas(snapshots);
        });
    }

    private void mostrarMascotas(QuerySnapshot snapshots) {
        mascotas.clear();
        if (snapshots != null) {
            for (DocumentSnapshot doc : snapshots.getDocuments()) {
                Mascota mascota = doc.toObject(Mascota.class);
                if (mascota != null) {
                    mascota.setId(doc.getId());
                    mascotas.add(mascota);
                }
            }
        }
        adapter.notifyDataSetChanged();
        textoVacio.setText("No hay mascotas registradas");
        textoVacio.setVisibility(mascotas.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (listener != null) {
            listener.remove();
            listener = null;
        }
    }
}
