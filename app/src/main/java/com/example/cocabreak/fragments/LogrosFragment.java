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
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState
    ) {
        super.onViewCreated(view, savedInstanceState);

        recyclerLogros = view.findViewById(R.id.recyclerLogros);
        tvCantidadLogros = view.findViewById(R.id.tvCantidadLogros);
        tvPorcentaje = view.findViewById(R.id.tvPorcentaje);
        progressLogros = view.findViewById(R.id.progressLogros);

        recyclerLogros.setLayoutManager(
                new GridLayoutManager(requireContext(), 2)
        );

        cargarLogros();
    }

    private void cargarLogros() {

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseDatabase.getInstance()
                .getReference("usuarios")
                .child(uid)
                .get()
                .addOnSuccessListener(snapshot -> {

                    long totalAgua = 0;
                    long totalCoca = 0;

                    long registrosAgua = 0;
                    long registrosCoca = 0;

                    if (snapshot.child("registrosAgua").exists()) {

                        for (DataSnapshot agua :
                                snapshot.child("registrosAgua").getChildren()) {

                            registrosAgua++;

                            Long cantidad =
                                    agua.child("cantidad")
                                            .getValue(Long.class);

                            if (cantidad != null) {
                                totalAgua += cantidad;
                            }
                        }
                    }

                    if (snapshot.child("registrosCoca").exists()) {

                        for (DataSnapshot coca :
                                snapshot.child("registrosCoca").getChildren()) {

                            registrosCoca++;

                            Long cantidad =
                                    coca.child("cantidad")
                                            .getValue(Long.class);

                            if (cantidad != null) {
                                totalCoca += cantidad;
                            }
                        }
                    }

                    ArrayList<Logro> logros =
                            new ArrayList<>();

                    boolean primerDia =
                            registrosAgua + registrosCoca > 0;

                    boolean hidratado =
                            totalAgua >= 2000;

                    boolean cambioInteligente =
                            totalAgua > totalCoca;

                    boolean usuarioActivo =
                            registrosAgua + registrosCoca >= 7;


                    boolean racha3Dias = false;

                    boolean racha7Dias = false;

                    boolean hidratacionSemanal =
                            totalAgua >= 10000;

                    boolean miembroActivo = false;

                    boolean apoyoEquipo = false;

                    boolean reduccion50 = false;

                    logros.add(new Logro(
                            "Primer Día",
                            "Completa tu primer día sin Coca-Cola",
                            primerDia
                    ));

                    logros.add(new Logro(
                            "Hidratado",
                            "Bebe 2 litros de agua en un día",
                            hidratado
                    ));

                    logros.add(new Logro(
                            "Cambio Inteligente",
                            "Sustituye una Coca-Cola por agua",
                            cambioInteligente
                    ));

                    logros.add(new Logro(
                            "Usuario Activo",
                            "Registra consumos durante varios días",
                            usuarioActivo
                    ));

                    logros.add(new Logro(
                            "Racha 3 Días",
                            "Mantente 3 días sin Coca-Cola",
                            racha3Dias
                    ));

                    logros.add(new Logro(
                            "Racha 7 Días",
                            "Mantente 7 días sin Coca-Cola",
                            racha7Dias
                    ));

                    logros.add(new Logro(
                            "Hidratación Semanal",
                            "Consume 10 litros de agua",
                            hidratacionSemanal
                    ));

                    logros.add(new Logro(
                            "Miembro Activo",
                            "Participa en tu grupo",
                            miembroActivo
                    ));


                    int desbloqueados = 0;

                    for (Logro logro : logros) {
                        if (logro.isDesbloqueado()) {
                            desbloqueados++;
                        }
                    }

                    boolean semanaPerfecta =
                            desbloqueados >= 10;

                    logros.add(new Logro(
                            "Semana Perfecta",
                            "Completa todos los retos",
                            semanaPerfecta
                    ));

                    boolean maestro =
                            desbloqueados >= 11;

                    logros.add(new Logro(
                            "Maestro CocaBreak",
                            "Desbloquea todos los logros",
                            maestro
                    ));

                    if (semanaPerfecta) desbloqueados++;
                    if (maestro) desbloqueados++;

                    int totalLogros =
                            logros.size();

                    tvCantidadLogros.setText(
                            desbloqueados +
                                    " de " +
                                    totalLogros +
                                    " logros"
                    );

                    progressLogros.setMax(totalLogros);
                    progressLogros.setProgress(desbloqueados);

                    int porcentaje =
                            (desbloqueados * 100)
                                    / totalLogros;

                    tvPorcentaje.setText(
                            porcentaje +
                                    "% completado"
                    );

                    recyclerLogros.setAdapter(
                            new LogroAdapter(logros)
                    );
                });
    }
}