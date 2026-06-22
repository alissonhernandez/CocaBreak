package com.example.cocabreak.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cocabreak.R;
import com.example.cocabreak.adapters.RetoAdapter;
import com.example.cocabreak.models.Reto;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;

public class RetosFragment extends Fragment {

    private RecyclerView recyclerRetos;
    private TextView txtRetosCompletados;
    private TextView txtPorcentaje;
    private ProgressBar progressGeneral;

    public RetosFragment() {
        super(R.layout.fragment_retos);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerRetos = view.findViewById(R.id.recyclerRetos);
        txtRetosCompletados = view.findViewById(R.id.txtRetosCompletados);
        txtPorcentaje = view.findViewById(R.id.txtPorcentaje);
        progressGeneral = view.findViewById(R.id.progressGeneral);

        recyclerRetos.setLayoutManager(new LinearLayoutManager(requireContext()));

        cargarRetosDinamicos();
    }

    private void cargarRetosDinamicos() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            return;
        }

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Calendar inicio = Calendar.getInstance();
        inicio.set(Calendar.HOUR_OF_DAY, 0);
        inicio.set(Calendar.MINUTE, 0);
        inicio.set(Calendar.SECOND, 0);
        inicio.set(Calendar.MILLISECOND, 0);
        long inicioDia = inicio.getTimeInMillis();

        FirebaseDatabase.getInstance()
                .getReference()
                .child("usuarios")
                .child(uid)
                .child("registrosAgua")
                .get()
                .addOnSuccessListener(snapshotAgua -> {
                    final int[] totalAgua = {0};
                    final int[] registrosAgua = {0};
                    final int[] totalSemanal = {0};

                    HashMap<String, Integer> aguaPorDia = new HashMap<>();
                    HashSet<String> diasActivos = new HashSet<>();

                    for (DataSnapshot dato : snapshotAgua.getChildren()) {
                        Long fecha = dato.child("fecha").getValue(Long.class);

                        if (fecha != null) {
                            Object cantidadObj = dato.child("cantidad").getValue();
                            int cantidad = 0;

                            if (cantidadObj instanceof Long) {
                                cantidad = ((Long) cantidadObj).intValue();
                            } else if (cantidadObj instanceof Integer) {
                                cantidad = (Integer) cantidadObj;
                            }

                            if (fecha >= inicioDia) {
                                totalAgua[0] += cantidad;
                                registrosAgua[0]++;
                            }

                            long hace7Dias = System.currentTimeMillis() - (7L * 24 * 60 * 60 * 1000);
                            if (fecha >= hace7Dias) {
                                totalSemanal[0] += cantidad;
                            }

                            SimpleDateFormat formato = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
                            String dia = formato.format(new Date(fecha));
                            diasActivos.add(dia);

                            int actual = aguaPorDia.containsKey(dia) ? aguaPorDia.get(dia) : 0;
                            aguaPorDia.put(dia, actual + cantidad);
                        }
                    }

                    FirebaseDatabase.getInstance()
                            .getReference()
                            .child("usuarios")
                            .child(uid)
                            .child("registrosCoca")
                            .get()
                            .addOnSuccessListener(snapshotCoca -> {
                                int cocaHoy = 0;


                                boolean tomoCocaHoy = false;
                                boolean tomoCocaAyer = false;
                                boolean tomoCocaAnteayer = false;

                                long inicioHoy = inicioDia;
                                long inicioAyer = inicioHoy - 86400000L;
                                long inicioAnteayer = inicioAyer - 86400000L;


                                int cocaSemanaActual = 0;
                                int cocaSemanaAnterior = 0;
                                long hoy = System.currentTimeMillis();
                                long inicioSemanaActual = hoy - (7L * 24 * 60 * 60 * 1000);
                                long inicioSemanaAnterior = hoy - (14L * 24 * 60 * 60 * 1000);


                                boolean tiene3DiasDeUso = diasActivos.size() >= 3;

                                for (DataSnapshot dato : snapshotCoca.getChildren()) {
                                    Long fecha = dato.child("fecha").getValue(Long.class);

                                    if (fecha != null) {
                                        if (fecha >= inicioHoy) {
                                            tomoCocaHoy = true;
                                        }
                                        if (fecha >= inicioAyer && fecha < inicioHoy) {
                                            tomoCocaAyer = true;
                                        }
                                        if (fecha >= inicioAnteayer && fecha < inicioAyer) {
                                            tomoCocaAnteayer = true;
                                        }

                                        Object cantidadObj = dato.child("cantidad").getValue();
                                        int cantidad = 0;

                                        if (cantidadObj instanceof Long) {
                                            cantidad = ((Long) cantidadObj).intValue();
                                        } else if (cantidadObj instanceof Integer) {
                                            cantidad = (Integer) cantidadObj;
                                        }

                                        if (fecha >= inicioDia) {
                                            cocaHoy += cantidad;
                                        }

                                        if (fecha >= inicioSemanaActual) {
                                            cocaSemanaActual += cantidad;
                                        } else if (fecha >= inicioSemanaAnterior) {
                                            cocaSemanaAnterior += cantidad;
                                        }
                                    }
                                }


                                boolean racha3Dias = tiene3DiasDeUso && !tomoCocaHoy && !tomoCocaAyer && !tomoCocaAnteayer;

                                int diasMeta = 0;
                                for (Integer ml : aguaPorDia.values()) {
                                    if (ml >= 2000) {
                                        diasMeta++;
                                    }
                                }

                                boolean metaCompleta = diasMeta >= 5;

                                boolean reduccion50 = false;
                                if (cocaSemanaAnterior > 0) {
                                    reduccion50 = cocaSemanaActual <= (cocaSemanaAnterior / 2);
                                }

                                final int[] misMensajes = {0};

                                int finalCocaHoy = cocaHoy;
                                boolean finalRacha3Dias = racha3Dias;
                                int finalDiasMeta = diasMeta;
                                boolean finalReduccion50 = reduccion50;

                                FirebaseDatabase.getInstance()
                                        .getReference("chatGeneral")
                                        .get()
                                        .addOnSuccessListener(snapshotChat -> {

                                            String miUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                            for (DataSnapshot mensaje : snapshotChat.getChildren()) {
                                                String uidMensaje = mensaje.child("uid").getValue(String.class);

                                                if (miUid.equals(uidMensaje)) {
                                                    misMensajes[0]++;
                                                }
                                            }

                                            boolean primerMensaje = misMensajes[0] >= 1;
                                            boolean apoyoEquipo = misMensajes[0] >= 5;

                                            ArrayList<Reto> retos = new ArrayList<>();

                                            retos.add(new Reto(
                                                    "Meta Completa",
                                                    "Cumplir meta de agua 5 días",
                                                    metaCompleta ? "Completado" : String.format(Locale.getDefault(), "%d / 5 días", finalDiasMeta),
                                                    false
                                            ));

                                            retos.add(new Reto(
                                                    "Un Día Sin Coca",
                                                    "No consumir Coca-Cola durante 24 horas",
                                                    finalCocaHoy == 0 ? "Completado" : "Pendiente",
                                                    false
                                            ));

                                            retos.add(new Reto(
                                                    "Registro Constante",
                                                    "Registrar agua 3 veces",
                                                    registrosAgua[0] >= 3 ? "Completado" : registrosAgua[0] + " / 3 registros",
                                                    false
                                            ));

                                            retos.add(new Reto(
                                                    "Hidratación Básica",
                                                    "Beber 2 litros de agua hoy",
                                                    totalAgua[0] >= 2000 ? "Completado" : totalAgua[0] + " / 2000 ml",
                                                    false
                                            ));

                                            retos.add(new Reto(
                                                    "Cambio Inteligente",
                                                    "Consumir más agua que Coca-Cola",
                                                    totalAgua[0] > finalCocaHoy ? "Completado" : "Pendiente",
                                                    false
                                            ));

                                            retos.add(new Reto(
                                                    "Racha de 3 Días",
                                                    "Mantenerte 3 días sin Coca-Cola",
                                                    finalRacha3Dias ? "Completado" : "Pendiente",
                                                    false
                                            ));

                                            retos.add(new Reto(
                                                    "Hidratación Semanal",
                                                    "Consumir 10 litros de agua",
                                                    totalSemanal[0] >= 10000 ? "Completado" : totalSemanal[0] + " / 10000 ml",
                                                    false
                                            ));

                                            retos.add(new Reto(
                                                    "Usuario Activo",
                                                    "Registrar consumos durante 7 días",
                                                    diasActivos.size() >= 7 ? "Completado" : diasActivos.size() + " / 7 días",
                                                    false
                                            ));

                                            retos.add(new Reto(
                                                    "Primer Mensaje",
                                                    "Compartir un avance en el grupo",
                                                    primerMensaje ? "Completado" : "Pendiente",
                                                    false
                                            ));

                                            retos.add(new Reto(
                                                    "Apoyo al Equipo",
                                                    "Enviar 5 mensajes al grupo",
                                                    apoyoEquipo ? "Completado" : misMensajes[0] + " / 5 mensajes",
                                                    false
                                            ));

                                            retos.add(new Reto(
                                                    "Reducción del 50%",
                                                    "Reducir el consumo semanal",
                                                    finalReduccion50 ? "Completado" : "Pendiente",
                                                    false
                                            ));

                                            boolean semanaPerfecta = metaCompleta
                                                    && finalCocaHoy == 0
                                                    && registrosAgua[0] >= 3
                                                    && totalAgua[0] >= 2000
                                                    && finalRacha3Dias
                                                    && totalSemanal[0] >= 10000
                                                    && diasActivos.size() >= 7
                                                    && primerMensaje
                                                    && apoyoEquipo;

                                            retos.add(new Reto(
                                                    "Semana Perfecta",
                                                    "Completar todos los retos",
                                                    semanaPerfecta ? "Completado" : "Pendiente",
                                                    false
                                            ));

                                            int completados = 0;
                                            for (Reto reto : retos) {
                                                if ("Completado".equals(reto.getProgreso())) {
                                                    completados++;
                                                }
                                            }

                                            int totalRetos = retos.size();
                                            txtRetosCompletados.setText(completados + " de " + totalRetos + " retos");
                                            progressGeneral.setMax(totalRetos);
                                            progressGeneral.setProgress(completados);

                                            int porcentaje = (completados * 100) / totalRetos;
                                            txtPorcentaje.setText(porcentaje + "% completado");

                                            recyclerRetos.setAdapter(new RetoAdapter(retos));
                                        });
                            });
                });
    }
}