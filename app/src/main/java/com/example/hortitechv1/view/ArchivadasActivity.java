package com.example.hortitechv1.view;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.hortitechv1.R;
import com.example.hortitechv1.controllers.BitacoraAdapter;
import com.example.hortitechv1.models.Bitacora;
import com.example.hortitechv1.network.ApiClient;
import com.example.hortitechv1.network.ApiBitacora;
import java.util.ArrayList;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArchivadasActivity extends AppCompatActivity implements BitacoraAdapter.BitacoraAdapterListener {

    private RecyclerView rvArchivadas;
    private BitacoraAdapter adapter;
    private List<Bitacora> bitacorasArchivadas = new ArrayList<>();
    private ApiBitacora apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archivadas);

        apiService = ApiClient.getClient().create(ApiBitacora.class);
        rvArchivadas = findViewById(R.id.rvArchivadas);
        rvArchivadas.setLayoutManager(new LinearLayoutManager(this));

        // --- CORRECCIÓN DEL CONSTRUCTOR ---
        adapter = new BitacoraAdapter(this, bitacorasArchivadas, this);
        rvArchivadas.setAdapter(adapter);

        cargarArchivadas();
    }

    private void cargarArchivadas() {
        apiService.getBitacoras(true).enqueue(new Callback<List<Bitacora>>() {
            @Override
            public void onResponse(Call<List<Bitacora>> call, Response<List<Bitacora>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    bitacorasArchivadas.clear();
                    bitacorasArchivadas.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ArchivadasActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<Bitacora>> call, Throwable t) {
                Toast.makeText(ArchivadasActivity.this, "Fallo de conexión.", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onArchiveClicked(Bitacora bitacora, int position) {
        apiService.desarchivarBitacora(bitacora.getId_publicacion()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    bitacorasArchivadas.remove(position);
                    adapter.notifyItemRemoved(position);
                    adapter.notifyItemRangeChanged(position, bitacorasArchivadas.size());
                    Toast.makeText(ArchivadasActivity.this, "Bitácora restaurada", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ArchivadasActivity.this, "Error al restaurar", Toast.LENGTH_SHORT).show();
                }
            }
            @Override public void onFailure(Call<ResponseBody> call, Throwable t) {}
        });
    }

    @Override public void onEditClicked(Bitacora bitacora) { /* No aplicable */ }
    @Override public void onDeleteClicked(Bitacora bitacora, int position) { /* No aplicable */ }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_OK);
        super.onBackPressed();
    }
}