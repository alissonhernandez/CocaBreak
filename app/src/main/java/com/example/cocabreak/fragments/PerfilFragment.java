package com.example.cocabreak.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cocabreak.R;

public class PerfilFragment extends Fragment {

    public PerfilFragment() {
        super(R.layout.fragment_perfil);
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        TextView btnMiInformacion =
                view.findViewById(R.id.btnMiInformacion);

        TextView btnCerrarSesion =
                view.findViewById(R.id.btnCerrarSesion);

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

        btnCerrarSesion.setOnClickListener(v -> {

            requireActivity().finish();

        });
    }
}