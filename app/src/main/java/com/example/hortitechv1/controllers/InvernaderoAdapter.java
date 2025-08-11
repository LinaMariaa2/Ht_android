package com.example.hortitechv1.controllers;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.hortitechv1.R;
import com.example.hortitechv1.models.Invernadero;
import com.example.hortitechv1.view.InvernaderoActivity;
import com.example.hortitechv1.view.ZonaActivity;

import java.util.List;
public class InvernaderoAdapter extends RecyclerView.Adapter<InvernaderoAdapter.ViewHolder> {
    private List<Invernadero> invernaderos;
    private Context context;
    public InvernaderoAdapter(Context context, List<Invernadero> invernaderos) {
        this.context = context;
        this.invernaderos = invernaderos;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_invernadero, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Invernadero inv = invernaderos.get(position);
        holder.tvNombre.setText(inv.getNombre());
        holder.tvEstado.setText("Estado: " + inv.getEstado());
        holder.tvDescripcion.setText(inv.getDescripcion());
        holder.tvZonasTotales.setText("Zonas totales: " + inv.getZonas_totales());
        holder.tvZonasActivas.setText("Zonas activas: " + inv.getZonas_activas());

        holder.btnZonas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ZonaActivity.class);
                intent.putExtra("invernadero_id", inv.getId_invernadero());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return invernaderos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvEstado, tvDescripcion, tvZonasTotales, tvZonasActivas;
        Button btnZonas;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombreInv);
            tvEstado = itemView.findViewById(R.id.tvEstadpInv);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcionesInv);
            tvZonasTotales = itemView.findViewById(R.id.tvZonasTotalesInv);
            tvZonasActivas = itemView.findViewById(R.id.tvZonasActivasInv);
            btnZonas = itemView.findViewById(R.id.btnInvZonas);
        }
    }
}