package com.example.cocabreak.fragments;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cocabreak.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class GrupoFragment extends Fragment {

    public GrupoFragment() {
        super(R.layout.fragment_grupos);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        MaterialButton btnIrChat =
                view.findViewById(R.id.btnIrChat);

        FloatingActionButton btnCrearGrupo =
                view.findViewById(R.id.btnCrearGrupo);

        btnIrChat.setOnClickListener(v ->

                requireActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(
                                R.id.fragmentContainer,
                                new ConversacionesFragment()
                        )
                        .addToBackStack(null)
                        .commit()

        );

        btnCrearGrupo.setOnClickListener(v ->

                requireActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(
                                R.id.fragmentContainer,
                                new CrearGrupoFragment()
                        )
                        .addToBackStack(null)
                        .commit()

        );
    }
}