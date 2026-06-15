package com.example.cocabreak.fragments;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cocabreak.R;
import com.example.cocabreak.adapters.ConversacionAdapter;
import com.example.cocabreak.models.Conversacion;

import java.util.ArrayList;
import java.util.List;

public class ConversacionesFragment extends Fragment {

    public ConversacionesFragment() {
        super(R.layout.fragment_conversaciones);
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        RecyclerView rvConversaciones =
                view.findViewById(R.id.rvConversaciones);

        List<Conversacion> conversaciones =
                new ArrayList<>();

        conversaciones.add(
                new Conversacion(
                        "Grupo CocaBreak",
                        "Juan: Ya tomé mis 2 litros ",
                        "09:30",
                        R.drawable.grupos
                )
        );

        conversaciones.add(
                new Conversacion(
                        "María",
                        "Gracias por el reto ",
                        "09:15",
                        R.drawable.grupos
                )
        );

        conversaciones.add(
                new Conversacion(
                        "Isabel",
                        "Ya registré mi agua",
                        "Ayer",
                        R.drawable.grupos
                )
        );

        conversaciones.add(
                new Conversacion(
                        "Amigos Saludables",
                        "Pedro: Vamos por esos 2 litros ",
                        "Ayer",
                        R.drawable.grupos
                )
        );

        ConversacionAdapter adapter =
                new ConversacionAdapter(conversaciones);

        rvConversaciones.setLayoutManager(
                new LinearLayoutManager(requireContext())
        );

        rvConversaciones.setAdapter(adapter);
    }
}