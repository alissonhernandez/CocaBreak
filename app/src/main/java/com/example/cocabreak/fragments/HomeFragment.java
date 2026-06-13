package com.example.cocabreak.fragments;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cocabreak.R;
import com.google.android.material.card.MaterialCardView;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        super(R.layout.fragment_home);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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

    private void abrirFragment(Fragment fragment) {

        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit();
    }
}