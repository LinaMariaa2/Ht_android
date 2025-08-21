package com.example.hortitechv1.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hortitechv1.R;
import com.example.hortitechv1.controllers.ProgramacionIlumiAdapter;
import com.example.hortitechv1.models.ProgramacionIluminacion;
import com.example.hortitechv1.network.ApiClient;
import com.example.hortitechv1.network.ApiProIluminacion;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProgramacionIluminacionActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Button btnNueva;
    private ProgramacionIlumiAdapter adapter;
    private List<ProgramacionIluminacion> listaProgramaciones = new ArrayList<>();
    private ApiProIluminacion api;
    private int idZona;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programacion_iluminacion);

        // Views
        recyclerView = findViewById(R.id.rvProgramacionIlumi);
        btnNueva = findViewById(R.id.btnNuevaProgramacionIlu);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // API
        api = ApiClient.getClient().create(ApiProIluminacion.class);

        // Adapter con listeners
        adapter = new ProgramacionIlumiAdapter(
                this,
                listaProgramaciones,
                new ProgramacionIlumiAdapter.OnItemClickListener() {
                    @Override
                    public void onActualizarClick(ProgramacionIluminacion programacion) {
                        actualizarProgramacion(programacion);
                    }

                    @Override
                    public void onDetenerClick(ProgramacionIluminacion programacion) {
                        detenerProgramacion(programacion);
                    }
                }
        );
        recyclerView.setAdapter(adapter);

        // Recibir idZona
        idZona = getIntent().getIntExtra("zona_id", -1);
        if (idZona != -1) {
            getProgramacionesFuturas();
        } else {
            Toast.makeText(this, "No se recibió el ID de la zona", Toast.LENGTH_SHORT).show();
        }

        // Nueva programación
        btnNueva.setOnClickListener(v -> {
            Intent intent = new Intent(ProgramacionIluminacionActivity.this, FormProIluminacionActivity.class);
            intent.putExtra("zona_id", idZona);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (idZona != -1) {
            getProgramacionesFuturas(); // refrescar lista al volver
        }
    }

    private void getProgramacionesFuturas() {
        api.getProgramacionesFuturas(idZona).enqueue(new Callback<List<ProgramacionIluminacion>>() {
            @Override
            public void onResponse(Call<List<ProgramacionIluminacion>> call, Response<List<ProgramacionIluminacion>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaProgramaciones.clear();
                    listaProgramaciones.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    Log.d("API_RESPONSE", "Programaciones recibidas: " + response.body().size());
                } else {
                    Toast.makeText(ProgramacionIluminacionActivity.this,
                            "Error del servidor: " + response.code(),
                            Toast.LENGTH_SHORT).show();
                    Log.e("API_RESPONSE", "Error: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<ProgramacionIluminacion>> call, Throwable t) {
                Toast.makeText(ProgramacionIluminacionActivity.this,
                        "Fallo de conexión: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
                Log.e("API_RESPONSE", "Fallo: ", t);
            }
        });
    }

    private void detenerProgramacion(ProgramacionIluminacion p) {
        api.eliminarProgramacion(p.getId_iluminacion()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ProgramacionIluminacionActivity.this,
                            "Programación eliminada",
                            Toast.LENGTH_SHORT).show();
                    getProgramacionesFuturas();
                } else {
                    Toast.makeText(ProgramacionIluminacionActivity.this,
                            "No se pudo eliminar",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ProgramacionIluminacionActivity.this,
                        "Error: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void actualizarProgramacion(ProgramacionIluminacion p) {
        Intent intent = new Intent(this, FormProIluminacionActivity.class);
        intent.putExtra("zona_id", idZona);
        intent.putExtra("programacion_id", p.getId_iluminacion());
        intent.putExtra("descripcion", p.getDescripcion());

        // Formato de fechas para el form
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        intent.putExtra("fecha_inicio", p.getFecha_inicio().format(formatter));
        intent.putExtra("fecha_fin", p.getFecha_finalizacion().format(formatter));

        startActivity(intent);
    }
}
