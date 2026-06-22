package com.example.cocabreak.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.net.Uri;
import android.widget.ImageView;

import com.example.cocabreak.R;
import com.example.cocabreak.activities.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PerfilFragment extends Fragment {

    public PerfilFragment() {
        super(R.layout.fragment_perfil);
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState
    ) {
        super.onViewCreated(view, savedInstanceState);

        TextView txtNombreUsuario = view.findViewById(R.id.txtNombreUsuario);
        ImageView imgPerfil = view.findViewById(R.id.imgPerfil);
        TextView btnMiInformacion = view.findViewById(R.id.btnMiInformacion);
        TextView btnHistorial = view.findViewById(R.id.btnHistorial);
        TextView btnCerrarSesion = view.findViewById(R.id.btnCerrarSesion);
        TextView tvAguaConsumida = view.findViewById(R.id.tvAguaConsumida);
        TextView tvCocaEvitada = view.findViewById(R.id.tvCocaEvitada);
        TextView tvRachaActual = view.findViewById(R.id.tvRachaActual);

        FirebaseAuth auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {

            String uid = auth.getCurrentUser().getUid();

            FirebaseDatabase.getInstance()
                    .getReference()
                    .child("usuarios")
                    .child(uid)
                    .addListenerForSingleValueEvent(
                            new ValueEventListener() {

                                @Override
                                public void onDataChange(
                                        @NonNull DataSnapshot snapshot
                                ) {

                                    if (!snapshot.exists()) {
                                        return;
                                    }

                                    String nombre =
                                            snapshot.child("nombre")
                                                    .getValue(String.class);

                                    if (nombre != null) {

                                        txtNombreUsuario.setText("Hola, " + nombre);
                                        long totalAgua = 0;
                                        long totalCoca = 0;

                                        if (snapshot.child("registrosAgua").exists()) {
                                            for (DataSnapshot agua :
                                                    snapshot.child("registrosAgua").getChildren()) {

                                                Long cantidad =
                                                        agua.child("cantidad")
                                                                .getValue(Long.class);

                                                if (cantidad != null) {
                                                    totalAgua += cantidad;
                                                }
                                            }
                                        }

                                        if (snapshot.child("registrosCoca").exists()) {
                                            for (DataSnapshot coca :
                                                    snapshot.child("registrosCoca").getChildren()) {

                                                Long cantidad =
                                                        coca.child("cantidad")
                                                                .getValue(Long.class);

                                                if (cantidad != null) {
                                                    totalCoca += cantidad;
                                                }
                                            }
                                        }

                                        tvAguaConsumida.setText(totalAgua + " ml");
                                        tvCocaEvitada.setText(totalCoca + " ml");


                                        java.util.HashSet<String> diasConCoca =
                                                new java.util.HashSet<>();

                                        java.text.SimpleDateFormat formato =
                                                new java.text.SimpleDateFormat(
                                                        "yyyyMMdd",
                                                        java.util.Locale.getDefault()
                                                );

                                        if (snapshot.child("registrosCoca").exists()) {
                                            for (DataSnapshot coca :
                                                    snapshot.child("registrosCoca").getChildren()) {

                                                Long fecha = coca.child("fecha").getValue(Long.class);

                                                if (fecha != null) {
                                                    String dia = formato.format(new java.util.Date(fecha));
                                                    diasConCoca.add(dia);
                                                }
                                            }
                                        }

                                        int racha = 0;
                                        java.util.Calendar calendario = java.util.Calendar.getInstance();


                                        for (int i = 0; i < 365; i++) {
                                            String diaActual = formato.format(calendario.getTime());

                                            if (diasConCoca.contains(diaActual)) {
                                                break;
                                            }

                                            racha++;
                                            calendario.add(java.util.Calendar.DAY_OF_YEAR, -1);
                                        }

                                        tvRachaActual.setText(racha + " días");
                                    }

                                    String foto =
                                            snapshot.child("fotoPerfil")
                                                    .getValue(String.class);

                                    if (foto != null && !foto.isEmpty()) {
                                        try {
                                            imgPerfil.setImageURI(Uri.parse(foto));
                                        } catch (Exception e) {
                                            imgPerfil.setImageResource(R.drawable.ic_person);
                                        }
                                    } else {
                                        imgPerfil.setImageResource(R.drawable.ic_person);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {}
                            }
                    );
        }

        btnMiInformacion.setOnClickListener(v ->
                requireActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, new EditarPerfilFragment())
                        .addToBackStack(null)
                        .commit()
        );

        btnHistorial.setOnClickListener(v ->
                requireActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, new HistorialFragment())
                        .addToBackStack(null)
                        .commit()
        );

        btnCerrarSesion.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(requireActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
}