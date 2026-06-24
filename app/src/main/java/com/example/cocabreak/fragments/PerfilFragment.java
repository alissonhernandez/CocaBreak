package com.example.cocabreak.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cocabreak.R;
import com.example.cocabreak.activities.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;

public class PerfilFragment extends Fragment {

    public PerfilFragment() {
        super(R.layout.fragment_perfil);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView txtNombreUsuario = view.findViewById(R.id.txtNombreUsuario);
        ImageView imgPerfil       = view.findViewById(R.id.imgPerfil);
        TextView btnMiInformacion = view.findViewById(R.id.btnMiInformacion);
        TextView btnHistorial     = view.findViewById(R.id.btnHistorial);
        TextView btnCerrarSesion  = view.findViewById(R.id.btnCerrarSesion);
        TextView tvAguaConsumida  = view.findViewById(R.id.tvAguaConsumida);
        TextView tvCocaEvitada    = view.findViewById(R.id.tvCocaEvitada);
        TextView tvRachaActual    = view.findViewById(R.id.tvRachaActual);

        FirebaseAuth auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            String uid = auth.getCurrentUser().getUid();

            FirebaseDatabase.getInstance()
                    .getReference("usuarios")
                    .child(uid)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (!snapshot.exists()) return;

                            String nombre = snapshot.child("nombre").getValue(String.class);
                            if (nombre != null) txtNombreUsuario.setText("Hola, " + nombre);

                            long totalAgua = 0, totalCoca = 0;
                            for (DataSnapshot a : snapshot.child("registrosAgua").getChildren()) {
                                Long c = a.child("cantidad").getValue(Long.class);
                                if (c != null) totalAgua += c;
                            }
                            for (DataSnapshot c : snapshot.child("registrosCoca").getChildren()) {
                                Long cant = c.child("cantidad").getValue(Long.class);
                                if (cant != null) totalCoca += cant;
                            }
                            tvAguaConsumida.setText(String.format(Locale.getDefault(), "%.2f L", totalAgua / 1000.0));
                            tvCocaEvitada.setText(String.format(Locale.getDefault(), "%.2f L", totalCoca / 1000.0));


                            SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
                            HashSet<String> diasConCoca = new HashSet<>();
                            HashSet<String> diasConAgua = new HashSet<>();

                            for (DataSnapshot c : snapshot.child("registrosCoca").getChildren()) {
                                Long f = c.child("fecha").getValue(Long.class);
                                if (f != null) diasConCoca.add(fmt.format(new Date(f)));
                            }
                            for (DataSnapshot a : snapshot.child("registrosAgua").getChildren()) {
                                Long f = a.child("fecha").getValue(Long.class);
                                if (f != null) diasConAgua.add(fmt.format(new Date(f)));
                            }

                            int racha = 0;
                            Calendar cal = Calendar.getInstance();
                            String hoy = fmt.format(cal.getTime());

                            // Si hoy no hay ningún registro todavía, empezamos a contar desde ayer
                            if (!diasConAgua.contains(hoy) && !diasConCoca.contains(hoy)) {
                                cal.add(Calendar.DAY_OF_YEAR, -1);
                            }

                            for (int i = 0; i < 365; i++) {
                                String dia = fmt.format(cal.getTime());
                                boolean tuvoAgua = diasConAgua.contains(dia);
                                boolean tuvoCoca = diasConCoca.contains(dia);

                                if (tuvoAgua && !tuvoCoca) {
                                    // Día perfecto: bebió agua y no tomó coca → suma a la racha
                                    racha++;
                                } else if (!tuvoAgua && !tuvoCoca) {
                                    // Día sin registros → corta la racha
                                    break;
                                } else {
                                    // Tomó coca (con o sin agua) → corta la racha
                                    break;
                                }
                                cal.add(Calendar.DAY_OF_YEAR, -1);
                            }

                            tvRachaActual.setText(racha + " días");


                            String foto = snapshot.child("fotoPerfil").getValue(String.class);
                            if (foto != null && !foto.isEmpty()) {
                                try {
                                    byte[] bytes = android.util.Base64.decode(foto, android.util.Base64.DEFAULT);
                                    android.graphics.Bitmap bitmap =
                                            android.graphics.BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                    if (bitmap != null) {
                                        imgPerfil.setImageBitmap(bitmap);
                                    } else {
                                        imgPerfil.setImageResource(R.drawable.ic_person);
                                    }
                                } catch (Exception e) {
                                    // Compatibilidad con fotos guardadas como URI en versiones anteriores
                                    try { imgPerfil.setImageURI(Uri.parse(foto)); }
                                    catch (Exception e2) { imgPerfil.setImageResource(R.drawable.ic_person); }
                                }
                            } else {
                                imgPerfil.setImageResource(R.drawable.ic_person);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {}
                    });
        }

        btnMiInformacion.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, new EditarPerfilFragment())
                        .addToBackStack(null).commit());

        btnHistorial.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, new HistorialFragment())
                        .addToBackStack(null).commit());

        btnCerrarSesion.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent i = new Intent(requireActivity(), LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        });
    }
}
