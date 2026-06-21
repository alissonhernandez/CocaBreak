package com.example.cocabreak.fragments;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cocabreak.R;
import com.example.cocabreak.adapters.GrupoAdapter;
import com.example.cocabreak.models.Grupo;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class GrupoFragment extends Fragment {

    private RecyclerView rvGrupos;

    private final ArrayList<Grupo> lista =
            new ArrayList<>();

    private GrupoAdapter adapter;

    public GrupoFragment() {
        super(R.layout.fragment_grupos);
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState
    ) {

        super.onViewCreated(view, savedInstanceState);

        rvGrupos =
                view.findViewById(R.id.rvGrupos);

        MaterialButton btnIrChat =
                view.findViewById(R.id.btnIrChat);

        FloatingActionButton btnCrearGrupo =
                view.findViewById(R.id.btnCrearGrupo);

        adapter =
                new GrupoAdapter(lista);

        rvGrupos.setLayoutManager(
                new LinearLayoutManager(
                        requireContext()
                )
        );

        rvGrupos.setAdapter(adapter);

        cargarGrupos();

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

    private void cargarGrupos() {

        FirebaseDatabase.getInstance()
                .getReference("grupos")
                .get()
                .addOnSuccessListener(snapshot -> {

                    lista.clear();

                    for (DataSnapshot ds :
                            snapshot.getChildren()) {

                        Grupo grupo =
                                ds.getValue(
                                        Grupo.class
                                );

                        if (grupo != null) {

                            lista.add(grupo);
                        }
                    }

                    adapter.notifyDataSetChanged();
                });
    }
}