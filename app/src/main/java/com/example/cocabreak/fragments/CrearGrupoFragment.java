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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class CrearGrupoFragment extends Fragment {

    public CrearGrupoFragment() {
        super(R.layout.fragment_crear_grupo);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton    btnRegresar  = view.findViewById(R.id.btnRegresar);
        EditText       etNombre     = view.findViewById(R.id.etNombreGrupo);
        EditText       etDescripcion = view.findViewById(R.id.etDescripcion);
        MaterialButton btnCrear     = view.findViewById(R.id.btnCrearGrupo);

        btnRegresar.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack());

        btnCrear.setOnClickListener(v -> {
            String nombre     = etNombre.getText().toString().trim();
            String descripcion = etDescripcion.getText().toString().trim();

            if (nombre.isEmpty()) {
                Toast.makeText(requireContext(), "Ingrese un nombre de grupo", Toast.LENGTH_SHORT).show();
                return;
            }

            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            String grupoId = FirebaseDatabase.getInstance()
                    .getReference("grupos").push().getKey();

            if (grupoId == null) {
                Toast.makeText(requireContext(), "Error al crear grupo", Toast.LENGTH_SHORT).show();
                return;
            }

            HashMap<String, Object> grupo = new HashMap<>();
            grupo.put("id",          grupoId);
            grupo.put("nombre",      nombre);
            grupo.put("descripcion", descripcion);
            grupo.put("creador",     uid);


            HashMap<String, Object> miembros = new HashMap<>();
            miembros.put(uid, true);
            grupo.put("miembros", miembros);

            FirebaseDatabase.getInstance()
                    .getReference("grupos")
                    .child(grupoId)
                    .setValue(grupo)
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(requireContext(), "Grupo creado correctamente", Toast.LENGTH_SHORT).show();
                        requireActivity().getSupportFragmentManager().popBackStack();
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(requireContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        });
    }
}
