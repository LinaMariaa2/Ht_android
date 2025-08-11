package com.example.hortitechv1.view;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hortitechv1.ItemSpacingDecoration;
import com.example.hortitechv1.R;
import com.example.hortitechv1.controllers.ZonaAdapter;
import com.example.hortitechv1.models.Invernadero;
import com.example.hortitechv1.models.Zona;
import com.example.hortitechv1.network.ApiClient;
import com.example.hortitechv1.network.ApiInvernaderos;
import com.example.hortitechv1.network.ApiZona;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ZonaActivity extends AppCompatActivity {

    private RecyclerView rvZonas;
    private ZonaAdapter adapter;
    private List<Invernadero> listaInvernaderos = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zona);

        rvZonas = findViewById(R.id.rvZonas);
        rvZonas.setLayoutManager(new LinearLayoutManager(this));
        rvZonas.addItemDecoration(new ItemSpacingDecoration(20));

        // Primero obtener los invernaderos, luego las zonas
        obtenerInvernaderos(() -> obtenerZonasDesdeApi());
    }

    private void obtenerInvernaderos(Runnable onComplete) {
        ApiInvernaderos api = ApiClient.getClient().create(ApiInvernaderos.class);
        Call<List<Invernadero>> call = api.getInvernaderos();

        call.enqueue(new Callback<List<Invernadero>>() {
            @Override
            public void onResponse(Call<List<Invernadero>> call, Response<List<Invernadero>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaInvernaderos = response.body();
                    onComplete.run();
                } else {
                    Toast.makeText(ZonaActivity.this, "Error al obtener invernaderos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Invernadero>> call, Throwable t) {
                Toast.makeText(ZonaActivity.this, "Error de red al obtener invernaderos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void obtenerZonasDesdeApi() {
        ApiZona api = ApiClient.getClient().create(ApiZona.class);
        Call<List<Zona>> call = api.getZonas();

        call.enqueue(new Callback<List<Zona>>() {
            @Override
            public void onResponse(Call<List<Zona>> call, Response<List<Zona>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Zona> zonas = response.body();
                    adapter = new ZonaAdapter(ZonaActivity.this, zonas, listaInvernaderos);
                    rvZonas.setAdapter(adapter);
                } else {
                    Toast.makeText(ZonaActivity.this, "Error al obtener zonas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Zona>> call, Throwable t) {
                Toast.makeText(ZonaActivity.this, "Error de red", Toast.LENGTH_SHORT).show();
                Log.e("ZonaActivity", "onFailure: ", t);
            }
        });
    }
}
