package com.example.cocabreak.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cocabreak.R;
import com.example.cocabreak.fragments.ChatFragment;
import com.example.cocabreak.models.Conversacion;

import java.util.List;

public class ConversacionAdapter
        extends RecyclerView.Adapter<ConversacionAdapter.ViewHolder> {

    private final List<Conversacion> lista;

    public ConversacionAdapter(List<Conversacion> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(
                        R.layout.item_conversacion,
                        parent,
                        false
                );

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position) {

        Conversacion conversacion = lista.get(position);

        holder.txtNombre.setText(
                conversacion.getNombre()
        );

        holder.txtMensaje.setText(
                conversacion.getUltimoMensaje()
        );

        holder.txtHora.setText(
                conversacion.getHora()
        );

        holder.imgPerfil.setImageResource(
                conversacion.getImagen()
        );

        holder.itemView.setOnClickListener(v -> {

            FragmentActivity activity =
                    (FragmentActivity) v.getContext();

            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(
                            R.id.fragmentContainer,
                            new ChatFragment()
                    )
                    .addToBackStack(null)
                    .commit();

        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder
            extends RecyclerView.ViewHolder {

        ImageView imgPerfil;
        TextView txtNombre;
        TextView txtMensaje;
        TextView txtHora;
        TextView txtNoLeidos;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgPerfil =
                    itemView.findViewById(R.id.imgPerfil);

            txtNombre =
                    itemView.findViewById(R.id.txtNombre);

            txtMensaje =
                    itemView.findViewById(R.id.txtMensaje);

            txtHora =
                    itemView.findViewById(R.id.txtHora);

            txtNoLeidos =
                    itemView.findViewById(R.id.txtNoLeidos);
        }
    }
}