package com.example.cocabreak.network;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("consejo")
    Call<ConsejoResponse> obtenerConsejo();
}