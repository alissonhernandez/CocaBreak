package com.example.cocabreak.fragments;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.cocabreak.R;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        super(R.layout.fragment_home);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CardView cardRetos = view.findViewById(R.id.cardRetos);
        CardView cardLogros = view.findViewById(R.id.cardLogros);
        CardView cardGrupo = view.findViewById(R.id.cardGrupo);
        CardView cardChat = view.findViewById(R.id.cardChat);

        cardRetos.setOnClickListener(v ->
                abrirFragment(new RetosFragment()));

        cardLogros.setOnClickListener(v ->
                abrirFragment(new LogrosFragment()));

        cardGrupo.setOnClickListener(v ->
                abrirFragment(new GrupoFragment()));

        cardChat.setOnClickListener(v ->
                abrirFragment(new ChatFragment()));
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