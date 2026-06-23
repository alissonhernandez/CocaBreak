package com.example.cocabreak.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cocabreak.R;
import com.example.cocabreak.models.NotificacionAgua;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NotificacionAdapter extends RecyclerView.Adapter<NotificacionAdapter.ViewHolder> {

    private final List<NotificacionAgua> lista;
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy  HH:mm", Locale.getDefault());

    public NotificacionAdapter(List<NotificacionAgua> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notificacion, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NotificacionAgua notif = lista.get(position);
        holder.titulo.setText(notif.getTitulo());
        holder.mensaje.setText(notif.getMensaje());
        holder.fecha.setText(sdf.format(new Date(notif.getFecha())));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titulo, mensaje, fecha;
        ImageView icono;

        ViewHolder(View v) {
            super(v);
            titulo = v.findViewById(R.id.txtTituloNotif);
            mensaje = v.findViewById(R.id.txtMensajeNotif);
            fecha = v.findViewById(R.id.txtFechaNotif);
            icono = v.findViewById(R.id.imgIconoNotif);
        }
    }
}