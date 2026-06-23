package com.example.cocabreak.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cocabreak.R;
import com.example.cocabreak.adapters.MensajeAdapter;
import com.example.cocabreak.models.Mensaje;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ChatFragment extends Fragment {

    private static final String ARG_GRUPO_ID     = "grupoId";
    private static final String ARG_GRUPO_NOMBRE = "grupoNombre";

    private List<Mensaje> mensajes;
    private MensajeAdapter adapter;
    private String grupoId;
    private String grupoNombre;

    public ChatFragment() {
        super(R.layout.fragment_chat);
    }

    public static ChatFragment newInstance(String grupoId, String grupoNombre) {
        ChatFragment f = new ChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_GRUPO_ID,     grupoId);
        args.putString(ARG_GRUPO_NOMBRE, grupoNombre);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            grupoId     = getArguments().getString(ARG_GRUPO_ID,     "chatGeneral");
            grupoNombre = getArguments().getString(ARG_GRUPO_NOMBRE, "Chat General");
        } else {
            grupoId     = "chatGeneral";
            grupoNombre = "Chat General";
        }


        TextView txtTituloChat = view.findViewById(R.id.txtTituloChat);
        if (txtTituloChat != null) txtTituloChat.setText(grupoNombre);


        ImageButton btnInvitar = view.findViewById(R.id.btnInvitar);
        if ("chatGeneral".equals(grupoId)) {
            btnInvitar.setVisibility(View.GONE);
        } else {
            btnInvitar.setVisibility(View.VISIBLE);
            btnInvitar.setOnClickListener(v -> {
                String finalGrupoId     = grupoId;
                String finalGrupoNombre = grupoNombre;
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer,
                                InvitarAGrupoFragment.newInstance(finalGrupoId, finalGrupoNombre))
                        .addToBackStack(null)
                        .commit();
            });
        }

        RecyclerView   rvMensajes = view.findViewById(R.id.rvMensajes);
        EditText       etMensaje  = view.findViewById(R.id.etMensaje);
        MaterialButton btnEnviar  = view.findViewById(R.id.btnEnviar);

        mensajes = new ArrayList<>();
        adapter  = new MensajeAdapter(mensajes);
        rvMensajes.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvMensajes.setAdapter(adapter);

        String ruta = "grupos/" + grupoId + "/mensajes";
        cargarMensajes(rvMensajes, ruta);

        btnEnviar.setOnClickListener(v -> {
            if (FirebaseAuth.getInstance().getCurrentUser() == null) return;
            String texto = etMensaje.getText().toString().trim();
            if (texto.isEmpty()) return;

            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseDatabase.getInstance().getReference()
                    .child("usuarios").child(uid).child("nombre")
                    .get()
                    .addOnSuccessListener(snap -> {
                        String nombre = snap.getValue(String.class);
                        if (nombre == null || nombre.isEmpty()) nombre = "Usuario";

                        HashMap<String, Object> mensaje = new HashMap<>();
                        mensaje.put("texto",  texto);
                        mensaje.put("uid",    uid);
                        mensaje.put("nombre", nombre);
                        mensaje.put("hora",   System.currentTimeMillis());

                        FirebaseDatabase.getInstance().getReference(ruta)
                                .push().setValue(mensaje);
                        etMensaje.setText("");
                    });
        });
    }

    private void cargarMensajes(RecyclerView rv, String ruta) {
        FirebaseDatabase.getInstance().getReference(ruta)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        mensajes.clear();
                        String miUid = FirebaseAuth.getInstance().getCurrentUser() != null
                                ? FirebaseAuth.getInstance().getCurrentUser().getUid() : "";

                        for (DataSnapshot dato : snapshot.getChildren()) {
                            String texto      = dato.child("texto").getValue(String.class);
                            String uidMsg     = dato.child("uid").getValue(String.class);
                            String nombre     = dato.child("nombre").getValue(String.class);
                            Long   horaMillis = dato.child("hora").getValue(Long.class);

                            boolean esMio = miUid.equals(uidMsg);
                            String horaTexto = horaMillis != null
                                    ? new SimpleDateFormat("hh:mm a", Locale.getDefault())
                                      .format(new Date(horaMillis))
                                    : "";

                            if (texto != null)
                                mensajes.add(new Mensaje(texto, esMio, nombre, horaTexto));
                        }
                        adapter.notifyDataSetChanged();
                        if (!mensajes.isEmpty()) rv.scrollToPosition(mensajes.size() - 1);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
    }
}
