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
import com.example.cocabreak.models.Logro;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class LogroAdapter
        extends RecyclerView.Adapter<LogroAdapter.ViewHolder> {

    private final List<Logro> listaLogros;

    public LogroAdapter(List<Logro> listaLogros) {
        this.listaLogros = listaLogros;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(
                        R.layout.item_logro,
                        parent,
                        false
                );

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position) {

        Logro logro = listaLogros.get(position);

        holder.tvTitulo.setText(
                logro.getTitulo()
        );

        holder.tvDescripcion.setText(
                logro.getDescripcion()
        );

        if (logro.isDesbloqueado()) {

            holder.imgLogro.setImageResource(
                    R.drawable.medalla
            );

            holder.tvEstado.setText(
                    "¡Desbloqueado!"
            );

            holder.tvEstado.setBackgroundResource(
                    R.drawable.bg_estado_logro
            );

            holder.tvEstado.setTextColor(
                    Color.WHITE
            );

            holder.cardLogro.setCardBackgroundColor(
                    Color.parseColor("#FFFDF5")
            );

            holder.cardLogro.setStrokeColor(
                    Color.parseColor("#FFD54F")
            );

            holder.tvTitulo.setTextColor(
                    Color.parseColor("#212121")
            );

        } else {

            holder.imgLogro.setImageResource(
                    R.drawable.bloqueado
            );

            holder.tvEstado.setText(
                    "Bloqueado"
            );

            holder.tvEstado.setBackgroundResource(
                    R.drawable.bg_estado_bloqueado
            );

            holder.tvEstado.setTextColor(
                    Color.parseColor("#616161")
            );

            holder.cardLogro.setCardBackgroundColor(
                    Color.parseColor("#F7F7F7")
            );

            holder.cardLogro.setStrokeColor(
                    Color.parseColor("#E0E0E0")
            );

            holder.tvTitulo.setTextColor(
                    Color.parseColor("#616161")
            );
        }
    }

    @Override
    public int getItemCount() {
        return listaLogros.size();
    }

    static class ViewHolder
            extends RecyclerView.ViewHolder {

        MaterialCardView cardLogro;
        ImageView imgLogro;
        TextView tvTitulo;
        TextView tvDescripcion;
        TextView tvEstado;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardLogro =
                    itemView.findViewById(
                            R.id.cardLogro
                    );

            imgLogro =
                    itemView.findViewById(
                            R.id.imgLogro
                    );

            tvTitulo =
                    itemView.findViewById(
                            R.id.tvTitulo
                    );

            tvDescripcion =
                    itemView.findViewById(
                            R.id.tvDescripcion
                    );

            tvEstado =
                    itemView.findViewById(
                            R.id.tvEstado
                    );
        }
    }
}