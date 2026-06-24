package com.example.cocabreak.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class EditarPerfilFragment extends Fragment {

    private EditText etNombre;
    private EditText etCorreo;
    private AutoCompleteTextView spDia;
    private AutoCompleteTextView spMes;
    private AutoCompleteTextView spAnio;
    private AutoCompleteTextView etGenero;
    private ImageView imgPerfilEditar;

    private String fotoBase64 = "";
    private static final int TAMANIO_FOTO = 300;

    private final ActivityResultLauncher<String>
            seleccionarImagenLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.GetContent(),
                    uri -> {

                        if (uri != null) {
                            procesarImagenSeleccionada(uri);
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

                        fotoBase64 = foto;

                        try {
                            byte[] bytes = android.util.Base64.decode(foto, android.util.Base64.DEFAULT);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            if (bitmap != null) {
                                imgPerfilEditar.setImageBitmap(bitmap);
                            }
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
        if (!fotoBase64.isEmpty()) {
            datos.put(
                    "fotoPerfil",
                    fotoBase64
            );
        }

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
    private void procesarImagenSeleccionada(Uri uri) {
        try {
            Bitmap original = decodificarBitmap(uri);
            if (original == null) {
                Toast.makeText(requireContext(), "No se pudo cargar la imagen", Toast.LENGTH_SHORT).show();
                return;
            }

            original = corregirRotacion(uri, original);
            Bitmap cuadrada = recortarCuadrado(original);
            Bitmap redimensionada = Bitmap.createScaledBitmap(cuadrada, TAMANIO_FOTO, TAMANIO_FOTO, true);

            imgPerfilEditar.setImageBitmap(redimensionada);
            fotoBase64 = bitmapABase64(redimensionada);

        } catch (Exception e) {
            Toast.makeText(requireContext(), "No se pudo procesar la imagen", Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap decodificarBitmap(Uri uri) throws IOException {
        try (InputStream input = requireContext().getContentResolver().openInputStream(uri)) {
            return BitmapFactory.decodeStream(input);
        }
    }

    private Bitmap corregirRotacion(Uri uri, Bitmap bitmap) {
        try (InputStream input = requireContext().getContentResolver().openInputStream(uri)) {
            if (input == null) return bitmap;

            ExifInterface exif = new ExifInterface(input);
            int orientacion = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            float grados = 0f;
            switch (orientacion) {
                case ExifInterface.ORIENTATION_ROTATE_90: grados = 90f; break;
                case ExifInterface.ORIENTATION_ROTATE_180: grados = 180f; break;
                case ExifInterface.ORIENTATION_ROTATE_270: grados = 270f; break;
            }

            if (grados == 0f) return bitmap;

            Matrix matrix = new Matrix();
            matrix.postRotate(grados);
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        } catch (Exception e) {
            return bitmap;
        }
    }

    private Bitmap recortarCuadrado(Bitmap bitmap) {
        int ancho = bitmap.getWidth();
        int alto = bitmap.getHeight();
        int lado = Math.min(ancho, alto);

        int x = (ancho - lado) / 2;
        int y = (alto - lado) / 2;

        return Bitmap.createBitmap(bitmap, x, y, lado, lado);
    }

    private String bitmapABase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        byte[] bytes = baos.toByteArray();
        return android.util.Base64.encodeToString(bytes, android.util.Base64.DEFAULT);
    }
}