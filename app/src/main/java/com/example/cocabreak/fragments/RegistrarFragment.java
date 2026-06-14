package com.example.cocabreak.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.cocabreak.R;
import com.example.cocabreak.utils.ConsejosSaludables;
import com.google.android.material.button.MaterialButton;

import java.util.Random;

public class RegistrarFragment extends Fragment {

    public RegistrarFragment() {
        super(R.layout.fragment_registrar);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        TextView txtConsejo =
                view.findViewById(R.id.txtConsejo);

        CardView cardCoca =
                view.findViewById(R.id.cardCoca);

        CardView cardAgua =
                view.findViewById(R.id.cardAgua);

        MaterialButton btnRegistrarCoca =
                view.findViewById(R.id.btnRegistrarCoca);

        MaterialButton btnRegistrarAgua =
                view.findViewById(R.id.btnRegistrarAgua);

        Random random = new Random();

        int indice =
                random.nextInt(
                        ConsejosSaludables.CONSEJOS.length
                );

        txtConsejo.setText(
                ConsejosSaludables.CONSEJOS[indice]
        );

        cardCoca.setOnClickListener(v ->
                abrirFragment(new RegistrarCocaFragment()));

        btnRegistrarCoca.setOnClickListener(v ->
                abrirFragment(new RegistrarCocaFragment()));

        cardAgua.setOnClickListener(v ->
                abrirFragment(new RegistrarAguaFragment()));

        btnRegistrarAgua.setOnClickListener(v ->
                abrirFragment(new RegistrarAguaFragment()));
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