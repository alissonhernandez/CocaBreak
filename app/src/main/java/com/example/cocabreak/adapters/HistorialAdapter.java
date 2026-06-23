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
import com.example.cocabreak.models.Historial;
import com.google.android.material.card.MaterialCardView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistorialAdapter extends RecyclerView.Adapter<HistorialAdapter.ViewHolder> {

    private final List<Historial> lista;

    public HistorialAdapter(List<Historial> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_historial, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Historial h = lista.get(position);


        String grupoFecha = obtenerGrupoFecha(h.getFecha());
        if (position == 0) {
            holder.txtGrupoFecha.setVisibility(View.VISIBLE);
            holder.txtGrupoFecha.setText(grupoFecha);
        } else {
            String anterior = obtenerGrupoFecha(lista.get(position - 1).getFecha());
            if (grupoFecha.equals(anterior)) {
                holder.txtGrupoFecha.setVisibility(View.GONE);
            } else {
                holder.txtGrupoFecha.setVisibility(View.VISIBLE);
                holder.txtGrupoFecha.setText(grupoFecha);
            }
        }


        String nombre = h.getNombre() != null ? h.getNombre() : "";
        holder.txtNombre.setText(nombre);


        boolean esCoca = "Coca-Cola".equals(h.getTipo());
        if (esCoca) {
            holder.txtTipo.setText("Coca-Cola");
            holder.txtTipo.setTextColor(Color.parseColor("#C62828"));
            holder.cardHistorial.setCardBackgroundColor(Color.parseColor("#FFF5F5"));
            holder.txtCantidad.setTextColor(Color.parseColor("#C62828"));
        } else {
            holder.txtTipo.setText("Agua");
            holder.txtTipo.setTextColor(Color.parseColor("#1565C0"));
            holder.cardHistorial.setCardBackgroundColor(Color.parseColor("#F5F9FF"));
            holder.txtCantidad.setTextColor(Color.parseColor("#1565C0"));
        }


        holder.txtCantidad.setText(h.getCantidad() + " ml");


        String hora = new SimpleDateFormat("hh:mm a", Locale.getDefault())
                .format(new Date(h.getFecha()));
        holder.txtFecha.setText(hora);


        if (nombre.contains("Mini")) {
            holder.imgProducto.setImageResource(R.drawable.mini);
        } else if (nombre.contains("Lata")) {
            holder.imgProducto.setImageResource(R.drawable.lata);
        } else if (nombre.contains("Vidrio")) {
            holder.imgProducto.setImageResource(R.drawable.vidrio);
        } else if (nombre.contains("1.5")) {
            holder.imgProducto.setImageResource(R.drawable.botella15);
        } else if (nombre.contains("2.5")) {
            holder.imgProducto.setImageResource(R.drawable.botella25);
        } else if (nombre.contains("3 L")) {
            holder.imgProducto.setImageResource(R.drawable.botella30);
        } else if (nombre.contains("Vaso")) {
            holder.imgProducto.setImageResource(R.drawable.vaso);
        } else {
            holder.imgProducto.setImageResource(R.drawable.botella);
        }
    }

    @Override
    public int getItemCount() { return lista.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardHistorial;
        ImageView imgProducto;
        TextView txtNombre, txtTipo, txtFecha, txtGrupoFecha, txtCantidad;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardHistorial  = itemView.findViewById(R.id.cardHistorial);
            imgProducto    = itemView.findViewById(R.id.imgProducto);
            txtNombre      = itemView.findViewById(R.id.txtNombre);
            txtTipo        = itemView.findViewById(R.id.txtTipo);
            txtFecha       = itemView.findViewById(R.id.txtFecha);
            txtGrupoFecha  = itemView.findViewById(R.id.txtGrupoFecha);
            txtCantidad    = itemView.findViewById(R.id.txtCantidad);
        }
    }

    private String obtenerGrupoFecha(long fechaMillis) {
        Calendar hoy   = Calendar.getInstance();
        Calendar fecha = Calendar.getInstance();
        fecha.setTimeInMillis(fechaMillis);

        if (hoy.get(Calendar.YEAR) == fecha.get(Calendar.YEAR)
                && hoy.get(Calendar.DAY_OF_YEAR) == fecha.get(Calendar.DAY_OF_YEAR)) {
            return "Hoy";
        }
        hoy.add(Calendar.DAY_OF_YEAR, -1);
        if (hoy.get(Calendar.YEAR) == fecha.get(Calendar.YEAR)
                && hoy.get(Calendar.DAY_OF_YEAR) == fecha.get(Calendar.DAY_OF_YEAR)) {
            return "Ayer";
        }
        return new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date(fechaMillis));
    }
}
