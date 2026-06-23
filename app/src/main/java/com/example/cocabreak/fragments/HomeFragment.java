package com.example.cocabreak.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.widget.ImageView;

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


    private TextView txtRetoActivo;
    private TextView txtRetoProgreso;
    private TextView txtLogroReciente;
    private TextView txtDescripcionLogro;

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


        txtRetoActivo = view.findViewById(R.id.txtRetoActivo);
        txtRetoProgreso = view.findViewById(R.id.txtRetoProgreso);
        txtLogroReciente = view.findViewById(R.id.txtLogroReciente);
        txtDescripcionLogro = view.findViewById(R.id.txtDescripcionLogro);

        cargarResumen();

        ImageView imgCampana = view.findViewById(R.id.imgCampana);
        if (imgCampana != null) {
            imgCampana.setOnClickListener(v -> abrirFragment(new NotificacionesFragment()));
        }

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
            cardChat.setOnClickListener(v -> abrirFragment(ChatFragment.newInstance("chatGeneral", "Chat General")));
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


                    java.util.Calendar calendar = java.util.Calendar.getInstance();
                    calendar.set(java.util.Calendar.HOUR_OF_DAY, 0);
                    calendar.set(java.util.Calendar.MINUTE, 0);
                    calendar.set(java.util.Calendar.SECOND, 0);
                    calendar.set(java.util.Calendar.MILLISECOND, 0);
                    long inicioHoy = calendar.getTimeInMillis();

                    final int[] totalAguaReferencia = {0};

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

                    int finalTotalCocaMl = totalMl;


                    FirebaseDatabase.getInstance()
                            .getReference()
                            .child("usuarios")
                            .child(uid)
                            .child("registrosAgua")
                            .get()
                            .addOnSuccessListener(snapshotAgua -> {

                                int totalAguaMl = 0;
                                boolean tieneRegistrosAgua = snapshotAgua.hasChildren();


                                java.util.Calendar calendarAgua = java.util.Calendar.getInstance();
                                calendarAgua.set(java.util.Calendar.HOUR_OF_DAY, 0);
                                calendarAgua.set(java.util.Calendar.MINUTE, 0);
                                calendarAgua.set(java.util.Calendar.SECOND, 0);
                                calendarAgua.set(java.util.Calendar.MILLISECOND, 0);
                                long inicioHoyAgua = calendarAgua.getTimeInMillis();

                                for (DataSnapshot dato : snapshotAgua.getChildren()) {
                                    Integer cantidad = dato.child("cantidad").getValue(Integer.class);
                                    Long fecha = dato.child("fecha").getValue(Long.class);

                                    if (cantidad != null && fecha != null && fecha >= inicioHoyAgua) {
                                        totalAguaMl += cantidad;
                                    }
                                }

                                double litrosAgua = totalAguaMl / 1000.0;
                                txtAguaHoy.setText(String.format(Locale.getDefault(), "%.1fL", litrosAgua));


                                if (totalAguaMl < 2000) {
                                    txtRetoActivo.setText("Hidratación Básica");
                                    txtRetoProgreso.setText(totalAguaMl + " / 2000 ml");
                                } else {
                                    txtRetoActivo.setText("Hidratación Básica");
                                    txtRetoProgreso.setText("Completado");
                                }


                                if (totalAguaMl > finalTotalCocaMl) {
                                    txtLogroReciente.setText("Cambio Inteligente");
                                    txtDescripcionLogro.setText("Elegiste agua sobre Coca-Cola");
                                } else if (totalAguaMl >= 2000) {
                                    txtLogroReciente.setText("Hidratado");
                                    txtDescripcionLogro.setText("Has alcanzado tu meta diaria");
                                } else if (tieneRegistrosAgua || snapshot.hasChildren()) {
                                    txtLogroReciente.setText("Primer Día");
                                    txtDescripcionLogro.setText("Has comenzado tu cambio de hábitos");
                                }
                            });
                });


        FirebaseDatabase.getInstance()
                .getReference("grupos")
                .get()
                .addOnSuccessListener(snapshot -> {
                    int cantidad = (int) snapshot.getChildrenCount();
                    txtMisGrupos.setText(cantidad + " grupos disponibles");
                });


        FirebaseDatabase.getInstance()
                .getReference("grupos/chatGeneral/mensajes")
                .get()
                .addOnSuccessListener(snapshot -> {
                    int cantidadMensajes = (int) snapshot.getChildrenCount();
                    txtMensajes.setText(cantidadMensajes + " mensajes");
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