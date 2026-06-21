package com.example.cocabreak.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cocabreak.R;
import com.example.cocabreak.models.Grupo;

import java.util.List;

public class GrupoAdapter
        extends RecyclerView.Adapter<GrupoAdapter.ViewHolder> {

    private final List<Grupo> lista;

    public GrupoAdapter(List<Grupo> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view =
                LayoutInflater.from(parent.getContext())
                        .inflate(
                                R.layout.item_grupo,
                                parent,
                                false
                        );

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position
    ) {

        Grupo grupo = lista.get(position);

        holder.txtNombreGrupo.setText(
                grupo.getNombre()
        );

        holder.txtDescripcionGrupo.setText(
                grupo.getDescripcion()
        );
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    static class ViewHolder
            extends RecyclerView.ViewHolder {

        TextView txtNombreGrupo;
        TextView txtDescripcionGrupo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtNombreGrupo =
                    itemView.findViewById(
                            R.id.txtNombreGrupo
                    );

            txtDescripcionGrupo =
                    itemView.findViewById(
                            R.id.txtDescripcionGrupo
                    );
        }
    }
}