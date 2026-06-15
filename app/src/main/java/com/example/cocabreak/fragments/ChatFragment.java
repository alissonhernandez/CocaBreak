package com.example.cocabreak.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cocabreak.R;
import com.example.cocabreak.adapters.MensajeAdapter;
import com.example.cocabreak.models.Mensaje;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {

    private List<Mensaje> mensajes;
    private MensajeAdapter adapter;

    public ChatFragment() {
        super(R.layout.fragment_chat);
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        RecyclerView rvMensajes =
                view.findViewById(R.id.rvMensajes);

        EditText etMensaje =
                view.findViewById(R.id.etMensaje);

        MaterialButton btnEnviar =
                view.findViewById(R.id.btnEnviar);

        mensajes = new ArrayList<>();

        mensajes.add(
                new Mensaje(
                        "Juan: Ya tomé mis 2 litros ",
                        false
                )
        );

        mensajes.add(
                new Mensaje(
                        "María: Excelente trabajo ",
                        false
                )
        );

        mensajes.add(
                new Mensaje(
                        "Yo: Voy por mi segundo vaso",
                        true
                )
        );

        adapter = new MensajeAdapter(mensajes);

        rvMensajes.setLayoutManager(
                new LinearLayoutManager(requireContext())
        );

        rvMensajes.setAdapter(adapter);

        btnEnviar.setOnClickListener(v -> {

            String texto =
                    etMensaje.getText().toString().trim();

            if (!texto.isEmpty()) {

                mensajes.add(
                        new Mensaje(
                                texto,
                                true
                        )
                );

                adapter.notifyItemInserted(
                        mensajes.size() - 1
                );

                rvMensajes.scrollToPosition(
                        mensajes.size() - 1
                );

                etMensaje.setText("");
            }
        });
    }
}