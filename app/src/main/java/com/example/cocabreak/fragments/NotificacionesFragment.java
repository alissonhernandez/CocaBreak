package com.example.cocabreak.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cocabreak.R;
import com.example.cocabreak.adapters.NotificacionAdapter;
import com.example.cocabreak.models.NotificacionAgua;
import com.example.cocabreak.notifications.WaterReminderScheduler;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NotificacionesFragment extends Fragment {

    private SwitchMaterial switchNotif;
    private TextView txtEstado;
    private TextView txtSinNotif;
    private RecyclerView recycler;
    private NotificacionAdapter adapter;
    private final List<NotificacionAgua> lista = new ArrayList<>();

    private int intervaloSeleccionado = WaterReminderScheduler.INTERVALO_DEFAULT;

    // Launcher para pedir permiso de notificaciones en Android 13+
    private final ActivityResultLauncher<String> permisosLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted -> {
                if (granted) {
                    activarConIntervalo(intervaloSeleccionado);
                } else {
                    Toast.makeText(requireContext(),
                            "Necesitas permitir notificaciones para recibir recordatorios",
                            Toast.LENGTH_LONG).show();
                    switchNotif.setChecked(false);
                }
            });

    public NotificacionesFragment() {
        super(R.layout.fragment_notificaciones);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        switchNotif = view.findViewById(R.id.switchNotificaciones);
        txtEstado = view.findViewById(R.id.txtEstadoActual);
        txtSinNotif = view.findViewById(R.id.txtSinNotificaciones);
        recycler = view.findViewById(R.id.recyclerNotificaciones);

        adapter = new NotificacionAdapter(lista);
        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        recycler.setAdapter(adapter);

        // Estado actual del switch
        boolean activo = WaterReminderScheduler.estaActivo(requireContext());
        switchNotif.setChecked(activo);
        actualizarTextoEstado(activo);

        switchNotif.setOnCheckedChangeListener((btn, isChecked) -> {
            if (isChecked) {
                pedirPermisoYActivar(intervaloSeleccionado);
            } else {
                WaterReminderScheduler.cancelar(requireContext());
                actualizarTextoEstado(false);
                Toast.makeText(requireContext(), "Recordatorios desactivados", Toast.LENGTH_SHORT).show();
            }
        });

        // Botones de intervalo
        view.findViewById(R.id.btn30min).setOnClickListener(v -> seleccionarIntervalo(30));
        view.findViewById(R.id.btn60min).setOnClickListener(v -> seleccionarIntervalo(60));
        view.findViewById(R.id.btn90min).setOnClickListener(v -> seleccionarIntervalo(90));
        view.findViewById(R.id.btn120min).setOnClickListener(v -> seleccionarIntervalo(120));

        // Limpiar historial
        view.findViewById(R.id.btnLimpiarHistorial).setOnClickListener(v -> limpiarHistorial());

        // Cargar historial desde Firebase
        cargarHistorial();
    }

    private void seleccionarIntervalo(int minutos) {
        intervaloSeleccionado = minutos;
        if (WaterReminderScheduler.estaActivo(requireContext())) {
            activarConIntervalo(minutos);
        }
    }

    private void pedirPermisoYActivar(int minutos) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                permisosLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
                return;
            }
        }
        activarConIntervalo(minutos);
    }

    private void activarConIntervalo(int minutos) {
        WaterReminderScheduler.activar(requireContext(), minutos);
        actualizarTextoEstado(true);

        String msg = minutos < 60
                ? "Recordatorio cada " + minutos + " minutos"
                : "Recordatorio cada " + (minutos / 60) + " hora(s)";
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private void actualizarTextoEstado(boolean activo) {
        if (activo) {
            int intervalo = WaterReminderScheduler.getIntervalo(requireContext());
            String texto = intervalo < 60
                    ? "Estado: activo · cada " + intervalo + " min"
                    : "Estado: activo · cada " + (intervalo / 60) + " hora(s)";
            txtEstado.setText(texto);
        } else {
            txtEstado.setText("Estado: desactivado");
        }
    }

    private void cargarHistorial() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        FirebaseDatabase.getInstance()
                .getReference("usuarios")
                .child(user.getUid())
                .child("historialNotificaciones")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        lista.clear();
                        for (DataSnapshot item : snapshot.getChildren()) {
                            NotificacionAgua notif = item.getValue(NotificacionAgua.class);
                            if (notif != null) lista.add(notif);
                        }
                        // Mostrar las más recientes primero
                        Collections.sort(lista, (a, b) -> Long.compare(b.getFecha(), a.getFecha()));
                        adapter.notifyDataSetChanged();

                        if (lista.isEmpty()) {
                            txtSinNotif.setVisibility(View.VISIBLE);
                            recycler.setVisibility(View.GONE);
                        } else {
                            txtSinNotif.setVisibility(View.GONE);
                            recycler.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });
    }

    private void limpiarHistorial() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        FirebaseDatabase.getInstance()
                .getReference("usuarios")
                .child(user.getUid())
                .child("historialNotificaciones")
                .removeValue()
                .addOnSuccessListener(v ->
                        Toast.makeText(requireContext(), "Historial limpiado", Toast.LENGTH_SHORT).show());
    }
}
