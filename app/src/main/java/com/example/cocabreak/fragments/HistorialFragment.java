package com.example.cocabreak.fragments;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cocabreak.R;
import com.example.cocabreak.adapters.HistorialAdapter;
import com.example.cocabreak.models.Historial;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class HistorialFragment extends Fragment {

    private RecyclerView rvHistorial;

    private MaterialButton btnTodos;
    private MaterialButton btnCoca;
    private MaterialButton btnAgua;

    private HistorialAdapter adapter;

    private final List<Historial> listaCompleta =
            new ArrayList<>();

    private final List<Historial> listaMostrar =
            new ArrayList<>();

    public HistorialFragment() {
        super(R.layout.fragment_historial);
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        rvHistorial =
                view.findViewById(R.id.rvHistorial);

        btnTodos =
                view.findViewById(R.id.btnTodos);

        btnCoca =
                view.findViewById(R.id.btnCoca);

        btnAgua =
                view.findViewById(R.id.btnAgua);

        rvHistorial.setLayoutManager(
                new LinearLayoutManager(getContext())
        );

        adapter =
                new HistorialAdapter(listaMostrar);

        rvHistorial.setAdapter(adapter);

        btnTodos.setOnClickListener(v ->
                mostrarTodos());

        btnCoca.setOnClickListener(v ->
                mostrarCoca());

        btnAgua.setOnClickListener(v ->
                mostrarAgua());

        cargarHistorial();
    }

    private void cargarHistorial() {

        String uid =
                FirebaseAuth.getInstance()
                        .getCurrentUser()
                        .getUid();

        listaCompleta.clear();

        FirebaseDatabase.getInstance()
                .getReference()
                .child("usuarios")
                .child(uid)
                .child("registrosCoca")
                .get()
                .addOnSuccessListener(snapshot -> {

                    for (DataSnapshot dato :
                            snapshot.getChildren()) {

                        String nombre =
                                dato.child("nombre")
                                        .getValue(String.class);

                        Long fecha =
                                dato.child("fecha")
                                        .getValue(Long.class);

                        listaCompleta.add(
                                new Historial(
                                        nombre,
                                        "Coca-Cola",
                                        fecha != null
                                                ? fecha
                                                : 0
                                )
                        );
                    }

                    cargarAgua(uid);
                });
    }

    private void cargarAgua(String uid) {

        FirebaseDatabase.getInstance()
                .getReference()
                .child("usuarios")
                .child(uid)
                .child("registrosAgua")
                .get()
                .addOnSuccessListener(snapshot -> {

                    for (DataSnapshot dato :
                            snapshot.getChildren()) {

                        Object cantidadObj =
                                dato.child("cantidad")
                                        .getValue();

                        int cantidad = 0;

                        if (cantidadObj instanceof Long) {
                            cantidad =
                                    ((Long) cantidadObj)
                                            .intValue();
                        }

                        String nombre;

                        if (cantidad == 250) {
                            nombre = "Vaso 250 ml";
                        } else {
                            nombre = "Botella 680 ml";
                        }

                        Long fecha =
                                dato.child("fecha")
                                        .getValue(Long.class);

                        listaCompleta.add(
                                new Historial(
                                        nombre,
                                        "Agua",
                                        fecha != null
                                                ? fecha
                                                : 0
                                )
                        );
                    }

                    mostrarTodos();
                });
    }

    private void mostrarTodos() {

        listaMostrar.clear();

        listaMostrar.addAll(
                listaCompleta
        );

        adapter.notifyDataSetChanged();
    }

    private void mostrarCoca() {

        listaMostrar.clear();

        for (Historial item :
                listaCompleta) {

            if ("Coca-Cola".equals(
                    item.getTipo())) {

                listaMostrar.add(item);
            }
        }

        adapter.notifyDataSetChanged();
    }

    private void mostrarAgua() {

        listaMostrar.clear();

        for (Historial item :
                listaCompleta) {

            if ("Agua".equals(
                    item.getTipo())) {

                listaMostrar.add(item);
            }
        }

        adapter.notifyDataSetChanged();
    }
}