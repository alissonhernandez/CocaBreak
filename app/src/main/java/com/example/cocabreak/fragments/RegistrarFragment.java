package com.example.cocabreak.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.cocabreak.R;
import com.example.cocabreak.network.ApiClient;
import com.example.cocabreak.network.ApiService;
import com.example.cocabreak.network.ConsejoResponse;
import com.google.android.material.button.MaterialButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrarFragment extends Fragment {

    public RegistrarFragment() {
        super(R.layout.fragment_registrar);
    }
    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView txtConsejo =
                view.findViewById(R.id.txtConsejo);
        CardView cardCoca =
                view.findViewById(R.id.cardCoca);
        CardView cardAgua =
                view.findViewById(R.id.cardAgua);
        MaterialButton btnRegistrarCoca =
                view.findViewById(R.id.btnRegistrarCoca);
        MaterialButton btnRegistrarAgua =
                view.findViewById(R.id.btnRegistrarAgua);
        cargarConsejo(txtConsejo);
        cardCoca.setOnClickListener(v ->
                abrirFragment(new RegistrarCocaFragment()));
        btnRegistrarCoca.setOnClickListener(v ->
                abrirFragment(new RegistrarCocaFragment()));
        cardAgua.setOnClickListener(v ->
                abrirFragment(new RegistrarAguaFragment()));
        btnRegistrarAgua.setOnClickListener(v ->
                abrirFragment(new RegistrarAguaFragment()));
    }
    private void cargarConsejo(TextView txtConsejo) {
        ApiService apiService =
                ApiClient.getClient()
                        .create(ApiService.class);
        apiService.obtenerConsejo()
                .enqueue(new Callback<ConsejoResponse>() {
                    @Override
                    public void onResponse(
                            Call<ConsejoResponse> call,
                            Response<ConsejoResponse> response) {
                        if (response.isSuccessful()
                                && response.body() != null) {
                            txtConsejo.setText(
                                    response.body().consejo
                            );
                        }
                    }
                    @Override
                    public void onFailure(
                            Call<ConsejoResponse> call,
                            Throwable t) {

                        txtConsejo.setText(
                                t.getMessage()
                        );
                    }
                });
    }

    private void abrirFragment(Fragment fragment) {

        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit();
    }
}