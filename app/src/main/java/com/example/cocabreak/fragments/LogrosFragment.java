package com.example.cocabreak.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cocabreak.R;
import com.example.cocabreak.adapters.LogroAdapter;
import com.example.cocabreak.models.Logro;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class LogrosFragment extends Fragment {

    private RecyclerView recyclerLogros;
    private TextView tvCantidadLogros;
    private TextView tvPorcentaje;
    private ProgressBar progressLogros;

    public LogrosFragment() {
        super(R.layout.fragment_logros);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerLogros  = view.findViewById(R.id.recyclerLogros);
        tvCantidadLogros = view.findViewById(R.id.tvCantidadLogros);
        tvPorcentaje    = view.findViewById(R.id.tvPorcentaje);
        progressLogros  = view.findViewById(R.id.progressLogros);

        recyclerLogros.setLayoutManager(new GridLayoutManager(requireContext(), 2));

        cargarLogros();
    }

    private void cargarLogros() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Paso 1: datos del usuario (agua + coca)
        FirebaseDatabase.getInstance()
                .getReference("usuarios")
                .child(uid)
                .get()
                .addOnSuccessListener(snapshot -> {

                    long totalAgua  = 0;
                    long totalCoca  = 0;
                    long regAgua    = 0;
                    long regCoca    = 0;

                    for (DataSnapshot d : snapshot.child("registrosAgua").getChildren()) {
                        regAgua++;
                        Long c = d.child("cantidad").getValue(Long.class);
                        if (c != null) totalAgua += c;
                    }
                    for (DataSnapshot d : snapshot.child("registrosCoca").getChildren()) {
                        regCoca++;
                        Long c = d.child("cantidad").getValue(Long.class);
                        if (c != null) totalCoca += c;
                    }

                    final long fAgua  = totalAgua;
                    final long fCoca  = totalCoca;
                    final long fReg   = regAgua + regCoca;

                    // Paso 2: buscar mensajes del usuario en todos los grupos
                    FirebaseDatabase.getInstance()
                            .getReference("grupos")
                            .get()
                            .addOnSuccessListener(gruposSnap -> {

                                int mensajesEnviados = 0;

                                for (DataSnapshot grupo : gruposSnap.getChildren()) {
                                    for (DataSnapshot msg : grupo.child("mensajes").getChildren()) {
                                        String uidMsg = msg.child("uid").getValue(String.class);
                                        if (uid.equals(uidMsg)) {
                                            mensajesEnviados++;
                                        }
                                    }
                                }

                                // --- Condiciones de cada logro ---
                                boolean primerDia        = fReg > 0;
                                boolean hidratado        = fAgua >= 2000;
                                boolean cambioInteligente = fAgua > fCoca;
                                boolean usuarioActivo    = fReg >= 7;
                                boolean hidratacionSem   = fAgua >= 10000;
                                boolean miembroActivo    = mensajesEnviados >= 1;   // ← CORREGIDO
                                boolean apoyoEquipo      = mensajesEnviados >= 5;   // ← CORREGIDO
                                boolean reduccion50      = fCoca > 0 && fAgua >= fCoca * 2;

                                ArrayList<Logro> logros = new ArrayList<>();

                                logros.add(new Logro("Primer Día",
                                        "Registra tu primer consumo",
                                        primerDia));

                                logros.add(new Logro("Hidratado",
                                        "Bebe 2 litros de agua en un día",
                                        hidratado));

                                logros.add(new Logro("Cambio Inteligente",
                                        "Bebe más agua que Coca-Cola",
                                        cambioInteligente));

                                logros.add(new Logro("Usuario Activo",
                                        "Registra consumos durante varios días",
                                        usuarioActivo));

                                logros.add(new Logro("Racha 3 Días",
                                        "Mantente 3 días sin Coca-Cola",
                                        false));

                                logros.add(new Logro("Racha 7 Días",
                                        "Mantente 7 días sin Coca-Cola",
                                        false));

                                logros.add(new Logro("Hidratación Semanal",
                                        "Consume 10 litros de agua en total",
                                        hidratacionSem));

                                logros.add(new Logro("Miembro Activo",
                                        "Envía un mensaje en tu grupo",
                                        miembroActivo));

                                logros.add(new Logro("Apoyo al Equipo",
                                        "Envía 5 mensajes en grupos",
                                        apoyoEquipo));

                                logros.add(new Logro("Reducción 50%",
                                        "Duplica tu consumo de agua vs Coca-Cola",
                                        reduccion50));

                                // Contar desbloqueados antes de los especiales
                                int desbloqueados = 0;
                                for (Logro l : logros) {
                                    if (l.isDesbloqueado()) desbloqueados++;
                                }

                                boolean semanaPerfecta = desbloqueados >= 10;
                                logros.add(new Logro("Semana Perfecta",
                                        "Desbloquea 10 logros",
                                        semanaPerfecta));
                                if (semanaPerfecta) desbloqueados++;

                                boolean maestro = desbloqueados >= 11;
                                logros.add(new Logro("Maestro CocaBreak",
                                        "Desbloquea todos los logros",
                                        maestro));
                                if (maestro) desbloqueados++;

                                int total = logros.size();
                                tvCantidadLogros.setText(desbloqueados + " de " + total + " logros");
                                progressLogros.setMax(total);
                                progressLogros.setProgress(desbloqueados);
                                tvPorcentaje.setText((desbloqueados * 100 / total) + "% completado");

                                recyclerLogros.setAdapter(new LogroAdapter(logros));
                            });
                });
    }
}