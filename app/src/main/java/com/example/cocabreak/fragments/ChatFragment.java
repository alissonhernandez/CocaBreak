package com.example.cocabreak.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cocabreak.R;

import java.util.ArrayList;

public class ChatFragment extends Fragment {

    private ArrayList<String> mensajes;
    private ArrayAdapter<String> adapter;

    public ChatFragment() {
        super(R.layout.fragment_chat);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView listMensajes =
                view.findViewById(R.id.listMensajes);

        EditText etMensaje =
                view.findViewById(R.id.etMensaje);

        Button btnEnviar =
                view.findViewById(R.id.btnEnviar);

        mensajes = new ArrayList<>();

        mensajes.add("Juan: Ya tomé 2 litros");
        mensajes.add("María: Voy muy bien hoy");

        adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_list_item_1,
                mensajes);

        listMensajes.setAdapter(adapter);

        btnEnviar.setOnClickListener(v -> {

            String texto = etMensaje.getText().toString();

            if (!texto.isEmpty()) {

                mensajes.add("Yo: " + texto);

                adapter.notifyDataSetChanged();

                etMensaje.setText("");
            }
        });
    }
}