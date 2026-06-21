package com.example.cocabreak.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.widget.ArrayAdapter;
import android.net.Uri;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;



import com.example.cocabreak.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class EditarPerfilFragment extends Fragment {

    private EditText etNombre;
    private EditText etCorreo;
    private AutoCompleteTextView spDia;
    private AutoCompleteTextView spMes;
    private AutoCompleteTextView spAnio;
    private AutoCompleteTextView etGenero;
    private ImageView imgPerfilEditar;
    private String fotoUri = "";

    private final ActivityResultLauncher<String>
            seleccionarImagenLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.GetContent(),
                    uri -> {

                        if (uri != null) {

                            fotoUri = uri.toString();

                            imgPerfilEditar.setImageURI(uri);
                        }
                    }
            );



    public EditarPerfilFragment() {
        super(R.layout.fragment_editar_perfil);
    }
    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState
    ) {
        super.onViewCreated(view, savedInstanceState);

        etNombre = view.findViewById(R.id.etNombre);
        etCorreo = view.findViewById(R.id.etCorreo);
        spDia = view.findViewById(R.id.spDia);
        spMes = view.findViewById(R.id.spMes);
        spAnio = view.findViewById(R.id.spAnio);
        spDia.setThreshold(0);
        spMes.setThreshold(0);
        spAnio.setThreshold(0);

        spDia.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) spDia.showDropDown();
        });

        spMes.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) spMes.showDropDown();
        });

        spAnio.setOnFocusChangeListener((v, hasFocus) -> {if (hasFocus) spAnio.showDropDown();
        });
        etGenero = view.findViewById(R.id.etGenero);
        imgPerfilEditar =
                view.findViewById(R.id.imgPerfilEditar);

        etCorreo.setEnabled(false);

        String[] generos = {
                "Femenino",
                "Masculino",
                "Prefiero no decirlo"
        };
        String[] dias = new String[31];

        for (int i = 1; i <= 31; i++) {
            dias[i - 1] = String.valueOf(i);
        }

        String[] meses = {
                "Enero",
                "Febrero",
                "Marzo",
                "Abril",
                "Mayo",
                "Junio",
                "Julio",
                "Agosto",
                "Septiembre",
                "Octubre",
                "Noviembre",
                "Diciembre"
        };

        String[] anios = new String[100];

        int indice = 0;

        for (int i = 2026; i >= 1927; i--) {
            anios[indice++] = String.valueOf(i);
        }

        spDia.setAdapter(
                new ArrayAdapter<>(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        dias
                )
        );

        spMes.setAdapter(
                new ArrayAdapter<>(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        meses
                )
        );

        spAnio.setAdapter(
                new ArrayAdapter<>(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        anios
                )
        );

        spDia.setOnClickListener(v -> spDia.showDropDown());
        spMes.setOnClickListener(v -> spMes.showDropDown());
        spAnio.setOnClickListener(v -> spAnio.showDropDown());

        ArrayAdapter<String> adapterGenero =
                new ArrayAdapter<>(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        generos
                );

        etGenero.setAdapter(adapterGenero);

        etGenero.setThreshold(0);

        etGenero.setOnFocusChangeListener(
                (v, hasFocus) -> {
                    if (hasFocus) {
                        etGenero.showDropDown();
                    }
                }
        );

        etGenero.setOnClickListener(v ->
                etGenero.showDropDown()
        );



        ImageButton btnRegresar =
                view.findViewById(R.id.btnRegresar);

        MaterialButton btnGuardar =
                view.findViewById(R.id.btnGuardar);
        ImageButton btnCambiarFoto =
                view.findViewById(R.id.btnCambiarFoto);

        btnCambiarFoto.setOnClickListener(v ->
                seleccionarImagenLauncher.launch("image/*")
        );




        btnRegresar.setOnClickListener(v ->
                requireActivity()
                        .getSupportFragmentManager()
                        .popBackStack()
        );


        cargarDatos();

        btnGuardar.setOnClickListener(v ->
                guardarDatos()
        );
    }

    private void cargarDatos() {

        String uid =
                FirebaseAuth.getInstance()
                        .getCurrentUser()
                        .getUid();

        FirebaseDatabase.getInstance()
                .getReference("usuarios")
                .child(uid)
                .get()
                .addOnSuccessListener(snapshot -> {

                    if (!snapshot.exists()) {
                        return;
                    }

                    etNombre.setText(
                            snapshot.child("nombre")
                                    .getValue(String.class)
                    );

                    etCorreo.setText(
                            snapshot.child("correo")
                                    .getValue(String.class)
                    );

                    String fecha =
                            snapshot.child("fechaNacimiento")
                                    .getValue(String.class);

                    if (fecha != null && fecha.contains("/")) {

                        String[] partes = fecha.split("/");

                        if (partes.length == 3) {

                            spDia.setText(partes[0], false);
                            spMes.setText(partes[1], false);
                            spAnio.setText(partes[2], false);
                        }
                    }

                    etGenero.setText(
                            snapshot.child("genero")
                                    .getValue(String.class)
                    );
                    String foto =
                            snapshot.child("fotoPerfil")
                                    .getValue(String.class);

                    if (foto != null && !foto.isEmpty()) {

                        fotoUri = foto;

                        try {

                            imgPerfilEditar.setImageURI(
                                    Uri.parse(foto)
                            );

                        } catch (Exception ignored) {

                        }
                    }

                });
    }

    private void guardarDatos() {

        String uid =
                FirebaseAuth.getInstance()
                        .getCurrentUser()
                        .getUid();

        HashMap<String, Object> datos =
                new HashMap<>();

        datos.put(
                "nombre",
                etNombre.getText().toString().trim()
        );

        datos.put(
                "correo",
                etCorreo.getText().toString().trim()
        );

        String fechaNacimiento =
                spDia.getText().toString() + "/" +
                        spMes.getText().toString() + "/" +
                        spAnio.getText().toString();

        datos.put(
                "fechaNacimiento",
                fechaNacimiento
        );
        datos.put(
                "genero",
                etGenero.getText().toString().trim()
        );
        datos.put(
                "fotoPerfil",
                fotoUri
        );



        FirebaseDatabase.getInstance()
                .getReference("usuarios")
                .child(uid)
                .updateChildren(datos)
                .addOnSuccessListener(unused -> {

                    Toast.makeText(
                            requireContext(),
                            "Datos guardados",
                            Toast.LENGTH_SHORT
                    ).show();

                    requireActivity()
                            .getSupportFragmentManager()
                            .popBackStack();
                });
    }
    }