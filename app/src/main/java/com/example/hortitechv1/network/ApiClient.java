package com.example.hortitechv1.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    // construye la instancia para llamar a los métodos.
    public static final String BASE_URL = "https://backendhortitech.onrender.com/"; // AL final siempre lleva otra /
    private static Retrofit retrofit = null;
    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()) // conivierte de json a objeto java
                    .build();
        }
        return retrofit;
    }
}
