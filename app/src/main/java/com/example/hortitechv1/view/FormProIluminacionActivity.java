package com.example.hortitechv1.view;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hortitechv1.R;
import com.example.hortitechv1.models.ProgramacionIluminacion;
import com.example.hortitechv1.network.ApiClient;
import com.example.hortitechv1.network.ApiProIluminacion;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormProIluminacionActivity extends AppCompatActivity {

    private EditText etDescripcion, etFechaInicio, etFechaFin;
    private Switch switchEstado;
    private Button btnCrear;
    private ApiProIluminacion api;
    private int idZona;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_pro_iluminacion);

        etDescripcion = findViewById(R.id.etP);
        etFechaInicio = findViewById(R.id.etFechaInicio);
        etFechaFin = findViewById(R.id.etFechaFin);
        switchEstado = findViewById(R.id.switchEstado);
        btnCrear = findViewById(R.id.btnCrear);

        api = ApiClient.getClient().create(ApiProIluminacion.class);

        idZona = getIntent().getIntExtra("zona_id", -1);
        if (idZona == -1) {
            Toast.makeText(this, "No se recibió el ID de la zona", Toast.LENGTH_SHORT).show();
            finish();
        }

        btnCrear.setOnClickListener(v -> crearProgramacion());
    }

    private void crearProgramacion() {
        String descripcion = etDescripcion.getText().toString();
        String fechaInicioStr = etFechaInicio.getText().toString();
        String fechaFinStr = etFechaFin.getText().toString();
        boolean estado = switchEstado.isChecked();

        // Formatear fechas a LocalDateTime
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime fechaInicio = LocalDateTime.parse(fechaInicioStr, formatter);
        LocalDateTime fechaFin = LocalDateTime.parse(fechaFinStr, formatter);

        ProgramacionIluminacion programacion = new ProgramacionIluminacion();
        programacion.setDescripcion(descripcion);
        programacion.setFecha_inicio(fechaInicio);
        programacion.setFecha_finalizacion(fechaFin);
        programacion.setEstado(estado);
        programacion.setId_zona(idZona);

        api.crearProgramacion(programacion).enqueue(new Callback<ProgramacionIluminacion>() {
            @Override
            public void onResponse(Call<ProgramacionIluminacion> call, Response<ProgramacionIluminacion> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(FormProIluminacionActivity.this,
                            "Programación creada correctamente", Toast.LENGTH_SHORT).show();
                    finish(); // cerrar Activity y volver al listado
                } else {
                    Toast.makeText(FormProIluminacionActivity.this,
                            "Error al crear: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProgramacionIluminacion> call, Throwable t) {
                Toast.makeText(FormProIluminacionActivity.this,
                        "Fallo de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
