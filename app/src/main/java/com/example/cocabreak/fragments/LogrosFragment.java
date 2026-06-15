package com.example.cocabreak.fragments;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cocabreak.R;
import com.example.cocabreak.adapters.LogroAdapter;
import com.example.cocabreak.models.Logro;

import java.util.ArrayList;

public class LogrosFragment extends Fragment {

    private RecyclerView recyclerLogros;

    public LogrosFragment() {
        super(R.layout.fragment_logros);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerLogros = view.findViewById(R.id.recyclerLogros);

        recyclerLogros.setLayoutManager(
                new GridLayoutManager(
                        requireContext(),
                        2
                )
        );

        ArrayList<Logro> logros = new ArrayList<>();

        logros.add(new Logro(
                "Primer Día",
                "Completa tu primer día sin Coca-Cola",
                true
        ));

        logros.add(new Logro(
                "Hidratado",
                "Bebe 2 litros de agua en un día",
                true
        ));

        logros.add(new Logro(
                "Cambio Inteligente",
                "Sustituye una Coca-Cola por agua",
                true
        ));

        logros.add(new Logro(
                "Usuario Activo",
                "Registra consumos durante varios días",
                true
        ));

        logros.add(new Logro(
                "Racha 3 Días",
                "Mantente 3 días sin Coca-Cola",
                false
        ));

        logros.add(new Logro(
                "Racha 7 Días",
                "Mantente 7 días sin Coca-Cola",
                false
        ));

        logros.add(new Logro(
                "Hidratación Semanal",
                "Consume 10 litros de agua",
                false
        ));

        logros.add(new Logro(
                "Miembro Activo",
                "Participa en tu grupo",
                false
        ));

        logros.add(new Logro(
                "Apoyo al Equipo",
                "Comenta publicaciones",
                false
        ));

        logros.add(new Logro(
                "Reducción 50%",
                "Reduce tu consumo semanal",
                false
        ));

        logros.add(new Logro(
                "Semana Perfecta",
                "Completa todos los retos",
                false
        ));

        logros.add(new Logro(
                "Maestro CocaBreak",
                "Desbloquea todos los logros",
                false
        ));

        LogroAdapter adapter =
                new LogroAdapter(logros);

        recyclerLogros.setAdapter(adapter);
    }

}
