package com.example.cocabreak.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cocabreak.R;
import com.example.cocabreak.models.Historial;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistorialAdapter
        extends RecyclerView.Adapter<HistorialAdapter.ViewHolder> {

    private final List<Historial> lista;

    public HistorialAdapter(List<Historial> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(
                parent.getContext()
        ).inflate(
                R.layout.item_historial,
                parent,
                false
        );

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position) {

        Historial historial = lista.get(position);

        holder.txtNombre.setText(
                historial.getNombre()
        );

        holder.txtTipo.setText(
                historial.getTipo()
        );

        String fecha =
                new SimpleDateFormat(
                        "dd/MM/yyyy HH:mm",
                        Locale.getDefault()
                ).format(
                        new Date(historial.getFecha())
                );

        holder.txtFecha.setText(fecha);

        String nombre =
                historial.getNombre() == null
                        ? ""
                        : historial.getNombre();

        // COCA-COLA
        if (nombre.contains("Mini")) {

            holder.imgProducto.setImageResource(
                    R.drawable.mini
            );

        } else if (nombre.contains("Lata")) {

            holder.imgProducto.setImageResource(
                    R.drawable.lata
            );

        } else if (nombre.contains("Vidrio")) {

            holder.imgProducto.setImageResource(
                    R.drawable.cocacola
            );

        } else if (nombre.contains("1.5")) {

            holder.imgProducto.setImageResource(
                    R.drawable.botella15
            );

        } else if (nombre.contains("2.5")) {

            holder.imgProducto.setImageResource(
                    R.drawable.botella25
            );

        } else if (nombre.contains("3 L")) {

            holder.imgProducto.setImageResource(
                    R.drawable.botella30
            );

        }

        // AGUA
        else if (nombre.contains("Vaso")) {

            holder.imgProducto.setImageResource(
                    R.drawable.vaso
            );

        } else if (nombre.contains("Botella")) {

            holder.imgProducto.setImageResource(
                    R.drawable.botella_agua
            );

        }

        // DEFAULT
        else {

            holder.imgProducto.setImageResource(
                    R.drawable.botella_agua
            );
        }
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder
            extends RecyclerView.ViewHolder {

        ImageView imgProducto;
        TextView txtNombre;
        TextView txtTipo;
        TextView txtFecha;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgProducto =
                    itemView.findViewById(
                            R.id.imgProducto
                    );

            txtNombre =
                    itemView.findViewById(
                            R.id.txtNombre
                    );

            txtTipo =
                    itemView.findViewById(
                            R.id.txtTipo
                    );

            txtFecha =
                    itemView.findViewById(
                            R.id.txtFecha
                    );
        }
    }
}