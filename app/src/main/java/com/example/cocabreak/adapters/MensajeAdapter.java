package com.example.cocabreak.adapters;

import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cocabreak.R;
import com.example.cocabreak.models.Mensaje;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class MensajeAdapter extends RecyclerView.Adapter<MensajeAdapter.ViewHolder> {

    private final List<Mensaje> mensajes;

    public MensajeAdapter(List<Mensaje> mensajes) {
        this.mensajes = mensajes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mensaje, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Mensaje mensaje = mensajes.get(position);

        holder.txtMensaje.setText(mensaje.getTexto());
        holder.txtHora.setText(mensaje.getHora());

        FrameLayout.LayoutParams params =
                (FrameLayout.LayoutParams) holder.cardMensaje.getLayoutParams();


        if (mensaje.isEnviadoPorMi()) {
            params.gravity = Gravity.END;


            holder.txtNombre.setVisibility(View.GONE);

            holder.cardMensaje.setCardBackgroundColor(Color.parseColor("#F44336"));
            holder.txtMensaje.setTextColor(Color.WHITE);
            holder.txtHora.setTextColor(Color.parseColor("#E0E0E0"));
        } else {
            params.gravity = Gravity.START;


            holder.txtNombre.setVisibility(View.VISIBLE);
            holder.txtNombre.setText(mensaje.getNombre());

            holder.cardMensaje.setCardBackgroundColor(Color.parseColor("#EEEEEE"));
            holder.txtMensaje.setTextColor(Color.BLACK);
            holder.txtNombre.setTextColor(Color.BLACK);
            holder.txtHora.setTextColor(Color.DKGRAY);
        }

        holder.cardMensaje.setLayoutParams(params);
    }

    @Override
    public int getItemCount() {
        return mensajes.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        MaterialCardView cardMensaje;
        TextView txtMensaje;
        TextView txtNombre;
        TextView txtHora;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardMensaje = itemView.findViewById(R.id.cardMensaje);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtMensaje = itemView.findViewById(R.id.txtMensaje);
            txtHora = itemView.findViewById(R.id.txtHora);
        }
    }
}