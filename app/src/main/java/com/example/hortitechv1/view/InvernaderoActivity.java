package com.example.hortitechv1.view;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hortitechv1.R;
import com.example.hortitechv1.controllers.InvernaderoAdapter;
import com.example.hortitechv1.models.Invernadero;
import com.example.hortitechv1.network.ApiClient;
import com.example.hortitechv1.network.ApiInvernaderos;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvernaderoActivity extends AppCompatActivity {
    RecyclerView rvInvernaderos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invernadero);

        rvInvernaderos = findViewById(R.id.rvInvernaderos);
        rvInvernaderos.setLayoutManager(new LinearLayoutManager(this));

        ApiInvernaderos api = ApiClient.getClient().create(ApiInvernaderos.class);
        Call<List<Invernadero>> call = api.getInvernaderos();

        call.enqueue(new Callback<List<Invernadero>>() {
            @Override
            public void onResponse(Call<List<Invernadero>> call, Response<List<Invernadero>> response) {
                if (response.isSuccessful()) {
                    List<Invernadero> lista = response.body();
                    Log.d("API_RESPONSE", new Gson().toJson(lista));
                    Toast.makeText(InvernaderoActivity.this, "Invernaderos: " + lista.size(), Toast.LENGTH_SHORT).show();

                    InvernaderoAdapter adapter = new InvernaderoAdapter(InvernaderoActivity.this, lista);
                    rvInvernaderos.setAdapter(adapter);
                } else {
                    Toast.makeText(InvernaderoActivity.this, "Error en respuesta", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Invernadero>> call, Throwable t) {
                Toast.makeText(InvernaderoActivity.this, "Fallo en conexión", Toast.LENGTH_SHORT).show();
                Log.e("API_FAIL", t.getMessage(), t);
            }
        });
    }
}