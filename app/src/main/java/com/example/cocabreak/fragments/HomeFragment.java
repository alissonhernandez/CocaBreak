package com.example.cocabreak.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cocabreak.R;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

public class HomeFragment extends Fragment {

    private TextView txtCocaHoy;
    private TextView txtAguaHoy;

    public HomeFragment() {
        super(R.layout.fragment_home);
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        txtCocaHoy = view.findViewById(R.id.txtCocaHoy);
        txtAguaHoy = view.findViewById(R.id.txtAguaHoy);

        cargarResumen();

        MaterialCardView cardReto = view.findViewById(R.id.cardReto);
        MaterialCardView cardLogro = view.findViewById(R.id.cardLogro);
        MaterialCardView cardGrupo = view.findViewById(R.id.cardGrupo);
        MaterialCardView cardChat = view.findViewById(R.id.cardChat);

        if (cardReto != null) {
            cardReto.setOnClickListener(v ->
                    abrirFragment(new RetosFragment()));
        }

        if (cardLogro != null) {
            cardLogro.setOnClickListener(v ->
                    abrirFragment(new LogrosFragment()));
        }

        if (cardGrupo != null) {
            cardGrupo.setOnClickListener(v ->
                    abrirFragment(new GrupoFragment()));
        }

        if (cardChat != null) {
            cardChat.setOnClickListener(v ->
                    abrirFragment(new ChatFragment()));
        }
    }

    private void cargarResumen() {

        String uid = FirebaseAuth.getInstance()
                .getCurrentUser()
                .getUid();

        // COCA-COLA
        FirebaseDatabase.getInstance()
                .getReference()
                .child("usuarios")
                .child(uid)
                .child("registrosCoca")
                .get()
                .addOnSuccessListener(snapshot -> {

                    int totalMl = 0;

                    for (DataSnapshot dato : snapshot.getChildren()) {

                        Integer cantidad =
                                dato.child("cantidad")
                                        .getValue(Integer.class);

                        if (cantidad != null) {
                            totalMl += cantidad;
                        }
                    }

                    double litros = totalMl / 1000.0;

                    txtCocaHoy.setText(
                            String.format(
                                    Locale.getDefault(),
                                    "%.1fL",
                                    litros
                            )
                    );
                });

        // AGUA
        FirebaseDatabase.getInstance()
                .getReference()
                .child("usuarios")
                .child(uid)
                .child("registrosAgua")
                .get()
                .addOnSuccessListener(snapshot -> {

                    int totalMl = 0;

                    for (DataSnapshot dato : snapshot.getChildren()) {

                        Integer cantidad =
                                dato.child("cantidad")
                                        .getValue(Integer.class);

                        if (cantidad != null) {
                            totalMl += cantidad;
                        }
                    }

                    double litros = totalMl / 1000.0;

                    txtAguaHoy.setText(
                            String.format(
                                    Locale.getDefault(),
                                    "%.1fL",
                                    litros
                            )
                    );
                });
    }

    private void abrirFragment(Fragment fragment) {

        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(
                        R.id.fragmentContainer,
                        fragment
                )
                .addToBackStack(null)
                .commit();
    }
}