package com.example.cocabreak.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cocabreak.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class RegistrarAguaFragment extends Fragment {

    private MaterialCardView cardVaso;
    private MaterialCardView cardBotella;
    private TextView tvVaso, tvBotella;
    private TextView tvLitrosActuales;
    private TextView tvPorcentaje;

    private int cantidadSeleccionada = 0;
    private String tipoSeleccionado = "";


    private DatabaseReference aguaRef;
    private ValueEventListener aguaListener;

    public RegistrarAguaFragment() {
        super(R.layout.fragment_registrar_agua);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cardVaso = view.findViewById(R.id.cardVaso);
        cardBotella = view.findViewById(R.id.cardBotella);
        tvVaso = view.findViewById(R.id.tvVaso);
        tvBotella = view.findViewById(R.id.tvBotella);
        tvLitrosActuales = view.findViewById(R.id.tvLitrosActuales);
        tvPorcentaje = view.findViewById(R.id.tvPorcentaje);


        cargarProgresoAgua();

        MaterialButton btnRegistrar = view.findViewById(R.id.btnRegistrar);
        View btnBack = view.findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v ->
                requireActivity()
                        .getSupportFragmentManager()
                        .popBackStack()
        );

        cardVaso.setOnClickListener(v -> {
            seleccionar(cardVaso, 250);
            tipoSeleccionado = "Vaso";
        });

        cardBotella.setOnClickListener(v -> {
            seleccionar(cardBotella, 680);
            tipoSeleccionado = "Botella";
        });

        btnRegistrar.setOnClickListener(v -> {
            if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                return;
            }

            if (cantidadSeleccionada == 0) {
                Toast.makeText(
                        getContext(),
                        "Selecciona una cantidad",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            HashMap<String, Object> registro = new HashMap<>();
            registro.put("cantidad", cantidadSeleccionada);
            registro.put("tipo", tipoSeleccionado);
            registro.put("fecha", System.currentTimeMillis());

            FirebaseDatabase.getInstance()
                    .getReference()
                    .child("usuarios")
                    .child(uid)
                    .child("registrosAgua")
                    .push()
                    .setValue(registro)
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(
                                getContext(),
                                "Agua registrada",
                                Toast.LENGTH_SHORT
                        ).show();

                        limpiarSeleccion();
                        cantidadSeleccionada = 0;
                        tipoSeleccionado = "";


                    });
        });
    }

    private void cargarProgresoAgua() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            return;
        }

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        aguaRef = FirebaseDatabase.getInstance()
                .getReference()
                .child("usuarios")
                .child(uid)
                .child("registrosAgua");

        aguaListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalMl = 0;


                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                long inicioDia = calendar.getTimeInMillis();

                for (DataSnapshot dato : snapshot.getChildren()) {
                    Long fecha = dato.child("fecha").getValue(Long.class);
                    Integer cantidad = dato.child("cantidad").getValue(Integer.class);


                    if (fecha != null && cantidad != null && fecha >= inicioDia) {
                        totalMl += cantidad;
                    }
                }

                double litros = totalMl / 1000.0;

                tvLitrosActuales.setText(
                        String.format(
                                Locale.getDefault(),
                                "%.1f Lt",
                                litros
                        )
                );

                int porcentaje = (int) ((totalMl * 100.0) / 2000);
                tvPorcentaje.setText(porcentaje + "%");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        aguaRef.addValueEventListener(aguaListener);
    }

    private void seleccionar(MaterialCardView seleccionada, int cantidad) {
        limpiarSeleccion();

        seleccionada.setCardBackgroundColor(Color.parseColor("#F5FAFF"));
        seleccionada.setStrokeWidth(3);
        seleccionada.setStrokeColor(Color.parseColor("#42A5F5"));

        if (seleccionada == cardVaso){
            tvVaso.setTextColor(Color.parseColor("#1E88F5"));
        } else {
            tvBotella.setTextColor(Color.parseColor("#1E88F5"));
        }

        cantidadSeleccionada = cantidad;
    }

    private void limpiarSeleccion() {
        MaterialCardView[] cards = {cardVaso, cardBotella};

        for (MaterialCardView card : cards) {
            if (card == null) continue;

            card.setCardBackgroundColor(Color.parseColor("#DCEEFF"));
            card.setStrokeWidth(1);
            card.setStrokeColor(Color.parseColor("#90CAF9"));
        }

        tvVaso.setTextColor(Color.parseColor("#212121"));
        tvBotella.setTextColor(Color.parseColor("#212121"));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (aguaRef != null && aguaListener != null) {
            aguaRef.removeEventListener(aguaListener);
        }
    }
}