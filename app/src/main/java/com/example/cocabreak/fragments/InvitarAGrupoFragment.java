package com.example.cocabreak.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cocabreak.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class InvitarAGrupoFragment extends Fragment {

    private static final String ARG_GRUPO_ID     = "grupoId";
    private static final String ARG_GRUPO_NOMBRE = "grupoNombre";

    public InvitarAGrupoFragment() {
        super(R.layout.fragment_invitar);
    }

    public static InvitarAGrupoFragment newInstance(String grupoId, String grupoNombre) {
        InvitarAGrupoFragment f = new InvitarAGrupoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_GRUPO_ID,     grupoId);
        args.putString(ARG_GRUPO_NOMBRE, grupoNombre);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String grupoId     = getArguments() != null ? getArguments().getString(ARG_GRUPO_ID,     "") : "";
        String grupoNombre = getArguments() != null ? getArguments().getString(ARG_GRUPO_NOMBRE, "") : "";

        ImageButton    btnRegresar = view.findViewById(R.id.btnRegresar);
        EditText       etCorreo    = view.findViewById(R.id.etCorreoInvitar);
        MaterialButton btnInvitar  = view.findViewById(R.id.btnEnviarInvitacion);

        btnRegresar.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack());

        btnInvitar.setOnClickListener(v -> {
            String correo = etCorreo.getText().toString().trim().toLowerCase();
            if (correo.isEmpty()) {
                Toast.makeText(requireContext(), "Ingresa un correo", Toast.LENGTH_SHORT).show();
                return;
            }


            String finalGrupoId     = grupoId;
            String finalGrupoNombre = grupoNombre;

            FirebaseDatabase.getInstance().getReference("usuarios")
                    .get()
                    .addOnSuccessListener(snapshot -> {
                        String uidDestino = null;

                        for (DataSnapshot ds : snapshot.getChildren()) {
                            String emailGuardado = ds.child("correo").getValue(String.class);
                            if (correo.equals(emailGuardado)) {
                                uidDestino = ds.getKey();
                                break;
                            }
                        }

                        if (uidDestino == null) {
                            Toast.makeText(requireContext(),
                                    "No se encontró un usuario con ese correo", Toast.LENGTH_SHORT).show();
                            return;
                        }


                        HashMap<String, Object> invitacion = new HashMap<>();
                        invitacion.put("id",          finalGrupoId);
                        invitacion.put("nombre",      finalGrupoNombre);
                        invitacion.put("descripcion", "Invitación al grupo");

                        FirebaseDatabase.getInstance()
                                .getReference("invitaciones")
                                .child(uidDestino)
                                .child(finalGrupoId)
                                .setValue(invitacion)
                                .addOnSuccessListener(u -> {
                                    Toast.makeText(requireContext(),
                                            "Invitación enviada", Toast.LENGTH_SHORT).show();
                                    requireActivity().getSupportFragmentManager().popBackStack();
                                });
                    });
        });
    }
}
