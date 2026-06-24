package com.example.cocabreak.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class HistorialFragment extends Fragment {

    private RecyclerView rvHistorial;

    private MaterialButton btnTodos;
    private MaterialButton btnCoca;
    private MaterialButton btnAgua;

    private TextView txtResumenCoca;
    private TextView txtResumenAgua;

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

        txtResumenCoca =
                view.findViewById(R.id.txtResumenCoca);

        txtResumenAgua =
                view.findViewById(R.id.txtResumenAgua);

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

                        Integer cantidad =
                                dato.child("cantidad")
                                        .getValue(Integer.class);

                        Long fecha =
                                dato.child("fecha")
                                        .getValue(Long.class);

                        listaCompleta.add(
                                new Historial(
                                        nombre,
                                        "Coca-Cola",
                                        fecha != null ? fecha : 0,
                                        cantidad != null ? cantidad : 0
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
                        } else if (cantidadObj instanceof Integer) {
                            cantidad =
                                    (Integer) cantidadObj;
                        }

                        Long fecha =
                                dato.child("fecha")
                                        .getValue(Long.class);

                        String nombre =
                                cantidad == 250
                                        ? "Vaso 250 ml"
                                        : "Botella 680 ml";

                        listaCompleta.add(
                                new Historial(
                                        nombre,
                                        "Agua",
                                        fecha != null ? fecha : 0,
                                        cantidad
                                )
                        );
                    }

                    Collections.sort(
                            listaCompleta,
                            (h1, h2) -> Long.compare(
                                    h2.getFecha(),
                                    h1.getFecha()
                            )
                    );

                    actualizarResumen();
                    mostrarTodos();
                });
    }

    private void actualizarBotones(String filtro) {

        btnTodos.setBackgroundTintList(
                android.content.res.ColorStateList.valueOf(
                        getResources().getColor(android.R.color.darker_gray)
                )
        );

        btnCoca.setBackgroundTintList(
                android.content.res.ColorStateList.valueOf(
                        getResources().getColor(android.R.color.darker_gray)
                )
        );

        btnAgua.setBackgroundTintList(
                android.content.res.ColorStateList.valueOf(
                        getResources().getColor(android.R.color.darker_gray)
                )
        );

        if ("TODOS".equals(filtro)) {

            btnTodos.setBackgroundTintList(
                    android.content.res.ColorStateList.valueOf(
                            getResources().getColor(R.color.coca_red)
                    )
            );

        } else if ("COCA".equals(filtro)) {

            btnCoca.setBackgroundTintList(
                    android.content.res.ColorStateList.valueOf(
                            getResources().getColor(R.color.coca_red)
                    )
            );

        } else if ("AGUA".equals(filtro)) {

            btnAgua.setBackgroundTintList(
                    android.content.res.ColorStateList.valueOf(
                            getResources().getColor(R.color.water_blue_btn)
                    )
            );
        }
    }

    private void mostrarTodos() {

        actualizarBotones("TODOS");

        listaMostrar.clear();
        listaMostrar.addAll(listaCompleta);

        adapter.notifyDataSetChanged();
    }

    private void mostrarCoca() {

        actualizarBotones("COCA");

        listaMostrar.clear();

        for (Historial item : listaCompleta) {

            if ("Coca-Cola".equals(item.getTipo())) {

                listaMostrar.add(item);
            }
        }

        adapter.notifyDataSetChanged();
    }

    private void mostrarAgua() {

        actualizarBotones("AGUA");

        listaMostrar.clear();

        for (Historial item : listaCompleta) {

            if ("Agua".equals(item.getTipo())) {

                listaMostrar.add(item);
            }
        }

        adapter.notifyDataSetChanged();
    }

    private void actualizarResumen() {

        int coca = 0;
        int agua = 0;

        int mlCoca = 0;
        int mlAgua = 0;

        for (Historial item :
                listaCompleta) {

            if ("Coca-Cola".equals(
                    item.getTipo())) {

                coca++;
                mlCoca += item.getCantidad();
            }

            if ("Agua".equals(
                    item.getTipo())) {

                agua++;
                mlAgua += item.getCantidad();
            }
        }

        double litrosCoca =
                mlCoca / 1000.0;

        double litrosAgua =
                mlAgua / 1000.0;

        txtResumenCoca.setText(coca + " consumos · " + String.format(Locale.getDefault(), "%.1f", litrosCoca) + " L");
        txtResumenAgua.setText(agua + " consumos · " + String.format(Locale.getDefault(), "%.1f", litrosAgua) + " L");
    }
}