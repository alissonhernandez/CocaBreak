package com.example.cocabreak.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cocabreak.R;
import com.example.cocabreak.models.Reto;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class RetoAdapter extends RecyclerView.Adapter<RetoAdapter.ViewHolder> {
    private final List<Reto> listaRetos;

    public RetoAdapter(List<Reto> listaRetos) {
        this.listaRetos = listaRetos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(
                        R.layout.item_reto,
                        parent,
                        false
                );

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position) {

        Reto reto = listaRetos.get(position);

        holder.tvTitulo.setText(
                reto.getTitulo()
        );

        holder.tvDescripcion.setText(
                reto.getDescripcion()
        );

        if (reto.isBloqueado()) {

            holder.imgEstado.setImageResource(
                    R.drawable.bloqueado
            );

            holder.tvProgreso.setText(
                    "Completa otros retos para desbloquear"
            );

            holder.tvProgreso.setTextColor(
                    Color.parseColor("#9E9E9E")
            );

            holder.cardReto.setCardBackgroundColor(
                    Color.parseColor("#F7F7F7")
            );

            holder.cardReto.setStrokeColor(
                    Color.parseColor("#E0E0E0")
            );

            holder.tvTitulo.setTextColor(
                    Color.parseColor("#616161")
            );

        } else {

            holder.imgEstado.setImageResource(
                    R.drawable.desbloqueado
            );

            holder.tvProgreso.setText(
                    reto.getProgreso()
            );

            holder.tvProgreso.setTextColor(
                    Color.parseColor("#1E88E5")
            );

            holder.cardReto.setCardBackgroundColor(
                    Color.parseColor("#EAF4FF")
            );

            holder.cardReto.setStrokeColor(
                    Color.parseColor("#D6EAFB")
            );

            holder.tvTitulo.setTextColor(
                    Color.parseColor("#212121")
            );
        }
    }

    @Override
    public int getItemCount() {
        return listaRetos.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        MaterialCardView cardReto;
        ImageView imgEstado;
        TextView tvTitulo;
        TextView tvDescripcion;
        TextView tvProgreso;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardReto =
                    itemView.findViewById(
                            R.id.cardReto
                    );

            imgEstado =
                    itemView.findViewById(
                            R.id.imgEstado
                    );

            tvTitulo =
                    itemView.findViewById(
                            R.id.tvTitulo
                    );

            tvDescripcion =
                    itemView.findViewById(
                            R.id.tvDescripcion
                    );

            tvProgreso =
                    itemView.findViewById(
                            R.id.tvProgreso
                    );
        }
    }

}
