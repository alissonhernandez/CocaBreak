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
    private TextView txtAzucar;
    private TextView txtCalorias;
    private TextView txtDinero;
    private TextView txtMisGrupos;
    private TextView txtMensajes;

    public HomeFragment() {
        super(R.layout.fragment_home);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txtCocaHoy = view.findViewById(R.id.txtCocaHoy);
        txtAguaHoy = view.findViewById(R.id.txtAguaHoy);
        txtAzucar = view.findViewById(R.id.txtAzucar);
        txtCalorias = view.findViewById(R.id.txtCalorias);
        txtDinero = view.findViewById(R.id.txtDinero);
        txtMisGrupos = view.findViewById(R.id.txtMisGrupos);
        txtMensajes = view.findViewById(R.id.txtMensajes);

        cargarResumen();

        MaterialCardView cardReto = view.findViewById(R.id.cardReto);
        MaterialCardView cardLogro = view.findViewById(R.id.cardLogro);
        MaterialCardView cardGrupo = view.findViewById(R.id.cardGrupo);
        MaterialCardView cardChat = view.findViewById(R.id.cardChat);

        if (cardReto != null) {
            cardReto.setOnClickListener(v -> abrirFragment(new RetosFragment()));
        }

        if (cardLogro != null) {
            cardLogro.setOnClickListener(v -> abrirFragment(new LogrosFragment()));
        }

        if (cardGrupo != null) {
            cardGrupo.setOnClickListener(v -> abrirFragment(new GrupoFragment()));
        }

        if (cardChat != null) {
            cardChat.setOnClickListener(v -> abrirFragment(new ChatFragment()));
        }
    }

    private void cargarResumen() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            return;
        }

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


        FirebaseDatabase.getInstance()
                .getReference()
                .child("usuarios")
                .child(uid)
                .child("registrosCoca")
                .get()
                .addOnSuccessListener(snapshot -> {

                    int totalMl = 0;
                    long ahora = System.currentTimeMillis();
                    long inicioHoy = ahora - (ahora % 86400000);

                    for (DataSnapshot dato : snapshot.getChildren()) {
                        Integer cantidad = dato.child("cantidad").getValue(Integer.class);
                        Long fecha = dato.child("fecha").getValue(Long.class);

                        if (cantidad != null && fecha != null && fecha >= inicioHoy) {
                            totalMl += cantidad;
                        }
                    }


                    double azucar = totalMl * 0.106;
                    double calorias = totalMl * 0.42;
                    double dinero = totalMl * 0.0015;
                    double litros = totalMl / 1000.0;

                    txtAzucar.setText(String.format(Locale.getDefault(), "%.0fg", azucar));
                    txtCalorias.setText(String.format(Locale.getDefault(), "%.0f kcal", calorias));
                    txtDinero.setText(String.format(Locale.getDefault(), "$%.2f", dinero));
                    txtCocaHoy.setText(String.format(Locale.getDefault(), "%.1fL", litros));
                });


        FirebaseDatabase.getInstance()
                .getReference()
                .child("usuarios")
                .child(uid)
                .child("registrosAgua")
                .get()
                .addOnSuccessListener(snapshot -> {

                    int totalMl = 0;
                    long ahora = System.currentTimeMillis();
                    long inicioHoy = ahora - (ahora % 86400000);

                    for (DataSnapshot dato : snapshot.getChildren()) {
                        Integer cantidad = dato.child("cantidad").getValue(Integer.class);
                        Long fecha = dato.child("fecha").getValue(Long.class);

                        if (cantidad != null && fecha != null && fecha >= inicioHoy) {
                            totalMl += cantidad;
                        }
                    }

                    double litros = totalMl / 1000.0;
                    txtAguaHoy.setText(String.format(Locale.getDefault(), "%.1fL", litros));
                });


        FirebaseDatabase.getInstance()
                .getReference("grupos")
                .get()
                .addOnSuccessListener(snapshot -> {
                    int cantidad = (int) snapshot.getChildrenCount();
                    txtMisGrupos.setText(cantidad + " grupos activos");
                });


        FirebaseDatabase.getInstance()
                .getReference("chatGeneral")
                .get()
                .addOnSuccessListener(snapshot -> {
                    int cantidadMensajes = (int) snapshot.getChildrenCount();
                    txtMensajes.setText(cantidadMensajes + " mensajes nuevos");
                });
    }

    private void abrirFragment(Fragment fragment) {
        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit();
    }
}