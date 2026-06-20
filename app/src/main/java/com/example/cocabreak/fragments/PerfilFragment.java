package com.example.cocabreak.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
            @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        TextView txtNombreUsuario =
                view.findViewById(R.id.txtNombreUsuario);


        TextView btnMiInformacion =
                view.findViewById(R.id.btnMiInformacion);
        TextView btnHistorial =
                view.findViewById(R.id.btnHistorial);

        TextView btnCerrarSesion =
                view.findViewById(R.id.btnCerrarSesion);

        FirebaseAuth auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {

            String uid = auth.getCurrentUser().getUid();

            FirebaseDatabase.getInstance()
                    .getReference()
                    .child("usuarios")
                    .child(uid)
                    .child("nombre")
                    .addListenerForSingleValueEvent(
                            new ValueEventListener() {

                                @Override
                                public void onDataChange(
                                        @NonNull DataSnapshot snapshot) {

                                    if (snapshot.exists()) {

                                        String nombre =
                                                snapshot.getValue(
                                                        String.class
                                                );

                                        txtNombreUsuario.setText(
                                                "Hola, " + nombre
                                        );
                                    }
                                }

                                @Override
                                public void onCancelled(
                                        @NonNull DatabaseError error) {

                                }
                            });
        }

        btnMiInformacion.setOnClickListener(v ->

                requireActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(
                                R.id.fragmentContainer,
                                new EditarPerfilFragment()
                        )
                        .addToBackStack(null)
                        .commit()
        );
        btnHistorial.setOnClickListener(v ->

                requireActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(
                                R.id.fragmentContainer,
                                new HistorialFragment()
                        )
                        .addToBackStack(null)
                        .commit()
        );

        btnCerrarSesion.setOnClickListener(v -> {

            FirebaseAuth.getInstance().signOut();

            Intent intent = new Intent(
                    requireActivity(),
                    LoginActivity.class
            );

            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK
            );

            startActivity(intent);
        });
    }
}