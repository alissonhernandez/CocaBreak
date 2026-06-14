package com.example.cocabreak.fragments;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cocabreak.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

public class RegistrarCocaFragment extends Fragment {

    private MaterialCardView cardMini;
    private MaterialCardView cardLata;
    private MaterialCardView cardVidrio;
    private MaterialCardView cardBotella15;
    private MaterialCardView cardBotella25;
    private MaterialCardView cardBotella30;


    private int cantidadSeleccionada = 0;
    private String nombreSeleccionado = "";

    public RegistrarCocaFragment() {
        super(R.layout.fragment_registrar_coca);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cardMini = view.findViewById(R.id.cardMini);
        cardLata = view.findViewById(R.id.cardLata);
        cardVidrio = view.findViewById(R.id.cardVidrio);
        cardBotella15 = view.findViewById(R.id.cardBotella15);
        cardBotella25 = view.findViewById(R.id.cardBotella25);
        cardBotella30 = view.findViewById(R.id.cardBotella30);

        MaterialButton btnRegistrar = view.findViewById(R.id.btnRegistrar);

        cardMini.setOnClickListener(v -> seleccionar(cardMini, "Mini 235 ml", 235));
        cardLata.setOnClickListener(v -> seleccionar(cardLata, "Lata 355 ml", 355));
        cardVidrio.setOnClickListener(v -> seleccionar(cardVidrio, "Vidrio 500 ml", 500));
        cardBotella15.setOnClickListener(v -> seleccionar(cardBotella15, "Botella 1.5 L", 1500));
        cardBotella25.setOnClickListener(v -> seleccionar(cardBotella25, "Botella 2.5 L", 2500));
        cardBotella30.setOnClickListener(v -> seleccionar(cardBotella30, "Botella 3 L", 3000));

        btnRegistrar.setOnClickListener(v -> {
            if (cantidadSeleccionada == 0) {
                Toast.makeText(getContext(), "Selecciona una Coca-Cola", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(getContext(), "Registraste: " + nombreSeleccionado, Toast.LENGTH_SHORT).show();
        });

        View btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v ->
                requireActivity()
                        .getSupportFragmentManager().popBackStack()
        );
    }

    private void seleccionar(MaterialCardView seleccionada, String nombre, int cantidad) {
        limpiarSeleccion();

        seleccionada.setCardBackgroundColor(Color.parseColor("#FFF4F4"));
        seleccionada.setStrokeWidth(dpToPx(2));

        seleccionada.setStrokeColor(Color.parseColor("#EF5350"));
        seleccionada.setCardElevation(dpToPx(6));
        TextView tvTitulo = buscarTextViewTitulo(seleccionada);
        if (tvTitulo != null) {
            tvTitulo.setTextColor(Color.parseColor("#EF5350"));
        }

        cantidadSeleccionada = cantidad;
        nombreSeleccionado = nombre;
    }

    private void limpiarSeleccion() {
        MaterialCardView[] cards = {
                cardMini,
                cardLata,
                cardVidrio,
                cardBotella15,
                cardBotella25,
                cardBotella30
        };

        for(MaterialCardView card : cards){
            if(card == null) continue;

            card.setCardBackgroundColor(Color.WHITE);

            card.setStrokeWidth(0);
            card.setStrokeColor(Color.TRANSPARENT);
            card.setCardElevation(dpToPx(4));

            TextView tvTitulo = buscarTextViewTitulo(card);
            if(tvTitulo != null){
                tvTitulo.setTextColor(Color.parseColor("#212121"));
            }
        }
    }

    private TextView buscarTextViewTitulo(MaterialCardView card) {
        try {
            ViewGroup contenedorHorizontal = (ViewGroup) card.getChildAt(0);
            ViewGroup contenedorVertical = (ViewGroup) contenedorHorizontal.getChildAt(0);
            return (TextView) contenedorVertical.getChildAt(0);
        } catch (Exception e) {
            return null;
        }
    }

    private int dpToPx(int dp) {
        if (getContext() == null) return dp;
        float density = getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }
}