package com.example.cocabreak.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cocabreak.R;
import com.example.cocabreak.adapters.RetoAdapter;
import com.example.cocabreak.models.Reto;

import java.util.ArrayList;

public class RetosFragment extends Fragment {

    private RecyclerView recyclerRetos;

    public RetosFragment() {
        super(R.layout.fragment_retos);
    }

    @Override
    public void onViewCreated(@NonNull android.view.View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerRetos = view.findViewById(R.id.recyclerRetos);

        recyclerRetos.setLayoutManager(
                new LinearLayoutManager(requireContext())
        );

        ArrayList<Reto> retos = new ArrayList<>();

        retos.add(new Reto(
                "Hidratación Básica",
                "Beber 2 litros de agua hoy",
                "1.5 / 2 L",
                false
        ));

        retos.add(new Reto(
                "Un Día Sin Coca",
                "No consumir Coca-Cola durante 24 horas",
                "Completado",
                false
        ));

        retos.add(new Reto(
                "Registro Constante",
                "Registrar agua 3 veces",
                "2 / 3 registros",
                false
        ));

        retos.add(new Reto(
                "Cambio Inteligente",
                "Sustituir una Coca-Cola por agua",
                "Pendiente",
                false
        ));

        retos.add(new Reto(
                "Racha de 3 Días",
                "Mantenerte 3 días sin Coca-Cola",
                "",
                true
        ));

        retos.add(new Reto(
                "Hidratación Semanal",
                "Consumir 10 litros de agua",
                "",
                true
        ));

        retos.add(new Reto(
                "Usuario Activo",
                "Registrar consumos durante 7 días",
                "",
                true
        ));

        retos.add(new Reto(
                "Meta Completa",
                "Cumplir meta de agua 5 días seguidos",
                "",
                true
        ));

        retos.add(new Reto(
                "Primer Mensaje",
                "Compartir un avance en el grupo",
                "",
                true
        ));

        retos.add(new Reto(
                "Apoyo al Equipo",
                "Comentar una publicación",
                "",
                true
        ));

        retos.add(new Reto(
                "Reducción del 50%",
                "Reducir el consumo semanal",
                "",
                true
        ));

        retos.add(new Reto(
                "Semana Perfecta",
                "Completar todos los retos",
                "",
                true
        ));

        RetoAdapter adapter =
                new RetoAdapter(retos);

        recyclerRetos.setAdapter(adapter);
    }
}