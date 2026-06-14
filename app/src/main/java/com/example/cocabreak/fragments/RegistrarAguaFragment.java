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

public class RegistrarAguaFragment extends Fragment {

    private MaterialCardView cardVaso;
    private MaterialCardView cardBotella;
    private TextView tvVaso, tvBotella;

    private int cantidadSeleccionada = 0;

    public RegistrarAguaFragment() {
        super(R.layout.fragment_registrar_agua);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cardVaso = view.findViewById(R.id.cardVaso);
        cardBotella = view.findViewById(R.id.cardBotella);
        tvVaso = view.findViewById(R.id.tvVaso);
        tvBotella = view.findViewById(R.id.tvBotella);

        MaterialButton btnRegistrar =
                view.findViewById(R.id.btnRegistrar);

        View btnBack = view.findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v ->
                requireActivity()
                        .getSupportFragmentManager()
                        .popBackStack()
        );

        cardVaso.setOnClickListener(v ->
                seleccionar(cardVaso, 250));

        cardBotella.setOnClickListener(v ->
                seleccionar(cardBotella, 680));

        btnRegistrar.setOnClickListener(v -> {

            if (cantidadSeleccionada == 0) {

                Toast.makeText(
                        getContext(),
                        "Selecciona una cantidad",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            Toast.makeText(
                    getContext(),
                    "Agua registrada: "
                            + cantidadSeleccionada + " ml",
                    Toast.LENGTH_SHORT
            ).show();
        });
    }

    private void seleccionar(MaterialCardView seleccionada,
                             int cantidad) {

        limpiarSeleccion();

        seleccionada.setCardBackgroundColor(
                Color.parseColor("#F5FAFF")
        );

        seleccionada.setStrokeWidth(3);

        seleccionada.setStrokeColor(
                Color.parseColor("#42A5F5")
        );

        if (seleccionada == cardVaso){
            tvVaso.setTextColor(Color.parseColor("#1E88F5"));
        }else {
            tvBotella.setTextColor(Color.parseColor("#1E88E5"));
        }

        cantidadSeleccionada = cantidad;
    }

    private void limpiarSeleccion() {

        MaterialCardView[] cards = {
                cardVaso,
                cardBotella
        };

        for (MaterialCardView card : cards) {

            if (card == null) continue;

            card.setCardBackgroundColor(Color.parseColor("#DCEEFF"));

            card.setStrokeWidth(1);

            card.setStrokeColor(
                    Color.parseColor("#90CAF9")
            );
        }

        tvVaso.setTextColor(Color.parseColor("#212121"));
        tvBotella.setTextColor(Color.parseColor("#212121"));
    }
}