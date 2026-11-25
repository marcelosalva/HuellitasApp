package com.example.huellitasapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MascotaAdapter extends RecyclerView.Adapter<MascotaAdapter.MascotaViewHolder> {
    private final List<Mascota> mascotas;

    public MascotaAdapter(List<Mascota> mascotas) {
        this.mascotas = mascotas;
    }

    @NonNull
    @Override
    public MascotaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mascota, parent, false);
        return new MascotaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MascotaViewHolder holder, int position) {
        Mascota mascota = mascotas.get(position);
        holder.txtNombre.setText(mascota.getNombre());
        holder.txtDatos.setText(mascota.getEspecie() + " - " + mascota.getEdad() + " años");
        holder.txtEstado.setText(mascota.getEstado());
        holder.itemView.setOnClickListener(v -> {
            // Solo se envía el ID; el detalle consulta Firestore.
            Intent intent = new Intent(v.getContext(), VerActivity.class);
            intent.putExtra(ListarActivity.EXTRA_ID, mascota.getId());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() { return mascotas.size(); }

    public static class MascotaViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre, txtDatos, txtEstado;
        public MascotaViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.item_nombre);
            txtDatos = itemView.findViewById(R.id.item_datos);
            txtEstado = itemView.findViewById(R.id.item_estado);
        }
    }
}
