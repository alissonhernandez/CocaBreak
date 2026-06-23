package com.example.cocabreak.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class GrupoFragment extends Fragment {

    private RecyclerView rvGrupos;
    private EditText etBuscar;

    private final ArrayList<Grupo> todosLosGrupos  = new ArrayList<>();
    private final ArrayList<Grupo> listaFiltrada   = new ArrayList<>();

    private GrupoAdapter adapter;
    private GrupoAdapter.Modo modoActual = GrupoAdapter.Modo.MIS_GRUPOS;

    private MaterialButton btnMisGrupos, btnDescubrir, btnInvitaciones;

    public GrupoFragment() {
        super(R.layout.fragment_grupos);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvGrupos         = view.findViewById(R.id.rvGrupos);
        etBuscar         = view.findViewById(R.id.etBuscarGrupo);
        btnMisGrupos     = view.findViewById(R.id.btnMisGrupos);
        btnDescubrir     = view.findViewById(R.id.btnDescubrir);
        btnInvitaciones  = view.findViewById(R.id.btnInvitaciones);
        FloatingActionButton btnCrearGrupo = view.findViewById(R.id.btnCrearGrupo);
        MaterialButton btnIrChat = view.findViewById(R.id.btnIrChat);

        rvGrupos.setLayoutManager(new LinearLayoutManager(requireContext()));


        setModo(GrupoAdapter.Modo.MIS_GRUPOS);
        cargarGrupos();


        btnMisGrupos.setOnClickListener(v -> {
            setModo(GrupoAdapter.Modo.MIS_GRUPOS);
            cargarGrupos();
        });
        btnDescubrir.setOnClickListener(v -> {
            setModo(GrupoAdapter.Modo.DESCUBRIR);
            cargarGrupos();
        });
        btnInvitaciones.setOnClickListener(v -> {
            setModo(GrupoAdapter.Modo.INVITACIONES);
            cargarInvitaciones();
        });


        etBuscar.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filtrar(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });


        btnIrChat.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer,
                                ChatFragment.newInstance("chatGeneral", "Chat General"))
                        .addToBackStack(null).commit());

        btnCrearGrupo.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, new CrearGrupoFragment())
                        .addToBackStack(null).commit());
    }

    private void setModo(GrupoAdapter.Modo modo) {
        modoActual = modo;


        int colorActivo   = 0xFFF44336;
        int colorInactivo = 0xFFBDBDBD;

        btnMisGrupos.setBackgroundTintList(
                android.content.res.ColorStateList.valueOf(
                        modo == GrupoAdapter.Modo.MIS_GRUPOS ? colorActivo : colorInactivo));
        btnDescubrir.setBackgroundTintList(
                android.content.res.ColorStateList.valueOf(
                        modo == GrupoAdapter.Modo.DESCUBRIR ? colorActivo : colorInactivo));
        btnInvitaciones.setBackgroundTintList(
                android.content.res.ColorStateList.valueOf(
                        modo == GrupoAdapter.Modo.INVITACIONES ? colorActivo : colorInactivo));
    }

    private void cargarGrupos() {
        String uid = FirebaseAuth.getInstance().getCurrentUser() != null
                ? FirebaseAuth.getInstance().getCurrentUser().getUid() : "";

        FirebaseDatabase.getInstance().getReference("grupos")
                .get()
                .addOnSuccessListener(snapshot -> {
                    todosLosGrupos.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Grupo grupo = ds.getValue(Grupo.class);
                        if (grupo == null) continue;

                        if (modoActual == GrupoAdapter.Modo.MIS_GRUPOS) {

                            boolean esMio = uid.equals(grupo.getCreador())
                                    || grupo.esMiembro(uid);
                            if (esMio) todosLosGrupos.add(grupo);

                        } else if (modoActual == GrupoAdapter.Modo.DESCUBRIR) {

                            boolean noEstoy = !uid.equals(grupo.getCreador())
                                    && !grupo.esMiembro(uid);
                            if (noEstoy) todosLosGrupos.add(grupo);
                        }
                    }
                    filtrar(etBuscar.getText().toString());
                });
    }

    private void cargarInvitaciones() {
        String uid = FirebaseAuth.getInstance().getCurrentUser() != null
                ? FirebaseAuth.getInstance().getCurrentUser().getUid() : "";

        FirebaseDatabase.getInstance()
                .getReference("invitaciones")
                .child(uid)
                .get()
                .addOnSuccessListener(snapshot -> {
                    todosLosGrupos.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Grupo grupo = ds.getValue(Grupo.class);
                        if (grupo != null) todosLosGrupos.add(grupo);
                    }
                    filtrar(etBuscar.getText().toString());
                });
    }

    private void filtrar(String texto) {
        listaFiltrada.clear();
        if (texto.isEmpty()) {
            listaFiltrada.addAll(todosLosGrupos);
        } else {
            String lower = texto.toLowerCase();
            for (Grupo g : todosLosGrupos) {
                String nom  = g.getNombre()      != null ? g.getNombre().toLowerCase()      : "";
                String desc = g.getDescripcion() != null ? g.getDescripcion().toLowerCase() : "";
                if (nom.contains(lower) || desc.contains(lower)) {
                    listaFiltrada.add(g);
                }
            }
        }
        adapter = new GrupoAdapter(listaFiltrada, modoActual);
        rvGrupos.setAdapter(adapter);
    }
}
