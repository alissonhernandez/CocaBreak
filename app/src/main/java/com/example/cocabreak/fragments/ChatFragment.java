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

    private List<Mensaje> mensajes;
    private MensajeAdapter adapter;

    public ChatFragment() {
        super(R.layout.fragment_chat);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView rvMensajes = view.findViewById(R.id.rvMensajes);
        EditText etMensaje = view.findViewById(R.id.etMensaje);
        MaterialButton btnEnviar = view.findViewById(R.id.btnEnviar);

        mensajes = new ArrayList<>();
        adapter = new MensajeAdapter(mensajes);

        rvMensajes.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvMensajes.setAdapter(adapter);

        cargarMensajes(rvMensajes);

        btnEnviar.setOnClickListener(v -> {
            if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                return;
            }

            String texto = etMensaje.getText().toString().trim();

            if (texto.isEmpty()) {
                return;
            }


            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            FirebaseDatabase.getInstance()
                    .getReference()
                    .child("usuarios")
                    .child(uid)
                    .child("nombre")
                    .get()
                    .addOnSuccessListener(snapshot -> {

                        String nombre = snapshot.getValue(String.class);


                        if (nombre == null || nombre.isEmpty()) {
                            nombre = "Usuario";
                        }

                        HashMap<String, Object> mensaje = new HashMap<>();
                        mensaje.put("texto", texto);
                        mensaje.put("uid", uid);
                        mensaje.put("nombre", nombre);
                        mensaje.put("hora", System.currentTimeMillis());

                        FirebaseDatabase.getInstance()
                                .getReference("chatGeneral")
                                .push()
                                .setValue(mensaje);

                        etMensaje.setText("");
                    });
        });
    }

    private void cargarMensajes(RecyclerView rvMensajes) {
        FirebaseDatabase.getInstance()
                .getReference("chatGeneral")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        mensajes.clear();

                        String miUid = "";
                        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                            miUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        }

                        for (DataSnapshot dato : snapshot.getChildren()) {
                            String texto = dato.child("texto").getValue(String.class);
                            String uidMensaje = dato.child("uid").getValue(String.class);
                            String nombre = dato.child("nombre").getValue(String.class);
                            Long horaMillis = dato.child("hora").getValue(Long.class);

                            boolean esMio = miUid.equals(uidMensaje);
                            String horaTexto = "";

                            if (horaMillis != null) {
                                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                                horaTexto = sdf.format(new Date(horaMillis));
                            }

                            if (texto != null) {
                                mensajes.add(new Mensaje(texto, esMio, nombre, horaTexto));
                            }
                        }

                        adapter.notifyDataSetChanged();

                        if (!mensajes.isEmpty()) {
                            rvMensajes.scrollToPosition(mensajes.size() - 1);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}