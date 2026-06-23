package com.example.cocabreak.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cocabreak.R;
import com.example.cocabreak.fragments.ChatFragment;
import com.example.cocabreak.models.Grupo;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class GrupoAdapter extends RecyclerView.Adapter<GrupoAdapter.ViewHolder> {

    public enum Modo { MIS_GRUPOS, DESCUBRIR, INVITACIONES }

    private final List<Grupo> lista;
    private final Modo modo;

    public GrupoAdapter(List<Grupo> lista, Modo modo) {
        this.lista = lista;
        this.modo  = modo;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_grupo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Grupo grupo = lista.get(position);

        holder.txtNombreGrupo.setText(grupo.getNombre() != null ? grupo.getNombre() : "Sin nombre");
        holder.txtDescripcionGrupo.setText(grupo.getDescripcion() != null ? grupo.getDescripcion() : "");

        String uid = FirebaseAuth.getInstance().getCurrentUser() != null
                ? FirebaseAuth.getInstance().getCurrentUser().getUid() : "";

        if (modo == Modo.MIS_GRUPOS) {

            holder.btnAccion.setVisibility(View.GONE);
            holder.itemView.setOnClickListener(v -> abrirChat(v, grupo));

        } else if (modo == Modo.DESCUBRIR) {

            holder.btnAccion.setVisibility(View.VISIBLE);
            holder.btnAccion.setText("Unirme");
            holder.btnAccion.setOnClickListener(v -> {
                if (uid.isEmpty()) return;
                FirebaseDatabase.getInstance()
                        .getReference("grupos")
                        .child(grupo.getId())
                        .child("miembros")
                        .child(uid)
                        .setValue(true)
                        .addOnSuccessListener(u -> {
                            Toast.makeText(v.getContext(), "Te uniste a " + grupo.getNombre(), Toast.LENGTH_SHORT).show();
                            holder.btnAccion.setText("Miembro ✓");
                            holder.btnAccion.setEnabled(false);

                            holder.itemView.setOnClickListener(vv -> abrirChat(vv, grupo));
                        });
            });

        } else if (modo == Modo.INVITACIONES) {

            holder.btnAccion.setVisibility(View.VISIBLE);
            holder.btnAccion.setText("Aceptar");
            holder.btnAccion.setOnClickListener(v -> {
                if (uid.isEmpty()) return;

                FirebaseDatabase.getInstance()
                        .getReference("grupos")
                        .child(grupo.getId())
                        .child("miembros")
                        .child(uid)
                        .setValue(true);

                FirebaseDatabase.getInstance()
                        .getReference("invitaciones")
                        .child(uid)
                        .child(grupo.getId())
                        .removeValue()
                        .addOnSuccessListener(u -> {
                            Toast.makeText(v.getContext(), "Te uniste a " + grupo.getNombre(), Toast.LENGTH_SHORT).show();
                            lista.remove(holder.getAdapterPosition());
                            notifyItemRemoved(holder.getAdapterPosition());
                        });
            });

            holder.txtDescripcionGrupo.setOnClickListener(v -> {

                FirebaseDatabase.getInstance()
                        .getReference("invitaciones")
                        .child(uid)
                        .child(grupo.getId())
                        .removeValue()
                        .addOnSuccessListener(u -> {
                            lista.remove(holder.getAdapterPosition());
                            notifyItemRemoved(holder.getAdapterPosition());
                        });
            });
        }
    }

    private void abrirChat(View v, Grupo grupo) {
        String id  = grupo.getId()     != null ? grupo.getId()     : "chatGeneral";
        String nom = grupo.getNombre() != null ? grupo.getNombre() : "Chat";

        ((FragmentActivity) v.getContext())
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, ChatFragment.newInstance(id, nom))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public int getItemCount() { return lista.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombreGrupo;
        TextView txtDescripcionGrupo;
        MaterialButton btnAccion;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombreGrupo      = itemView.findViewById(R.id.txtNombreGrupo);
            txtDescripcionGrupo = itemView.findViewById(R.id.txtDescripcionGrupo);
            btnAccion           = itemView.findViewById(R.id.btnAccion);
        }
    }
}
