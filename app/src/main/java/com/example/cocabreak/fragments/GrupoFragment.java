package com.example.cocabreak.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cocabreak.R;

public class GrupoFragment extends Fragment {

    public GrupoFragment() {
        super(R.layout.fragment_grupos);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnIrChat = view.findViewById(R.id.btnIrChat);

        btnIrChat.setOnClickListener(v -> {

            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer,
                            new ChatFragment())
                    .addToBackStack(null)
                    .commit();

        });
    }
}