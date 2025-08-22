package com.example.hortitechv1.view;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hortitechv1.R;
import com.example.hortitechv1.models.ProgramacionIluminacion;
import com.example.hortitechv1.network.ApiClient;
import com.example.hortitechv1.network.ApiProIluminacion;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormProIluminacionActivity extends AppCompatActivity {

    private EditText etDescripcion, etFechaInicio, etFechaFin;
    private Button btnAccion, btnCancelar;
    private TextView tvTitulo;
    private ApiProIluminacion api;
    private int idZona;
    private int programacionId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_pro_iluminacion);

        // Inicializar vistas
        etDescripcion = findViewById(R.id.etP);
        etFechaInicio = findViewById(R.id.etFechaInicio);
        etFechaFin = findViewById(R.id.etFechaFin);
        btnAccion = findViewById(R.id.btnCrear);
        btnCancelar = findViewById(R.id.btnCancelarFormI);
        tvTitulo = findViewById(R.id.tvTituloForm);

        // Inicializar API
        api = ApiClient.getClient().create(ApiProIluminacion.class);

        // Recibir zona_id
        idZona = getIntent().getIntExtra("zona_id", -1);
        if (idZona == -1) {
            Toast.makeText(this, "No se recibió el ID de la zona", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Revisamos si venimos a EDITAR
        programacionId = getIntent().getIntExtra("programacion_id", -1);
        if (programacionId != -1) {
            // Venimos a editar → rellenamos campos
            String descripcion = getIntent().getStringExtra("descripcion");
            String fechaInicio = getIntent().getStringExtra("fecha_inicio");
            String fechaFin = getIntent().getStringExtra("fecha_fin");

            etDescripcion.setText(descripcion);
            etFechaInicio.setText(fechaInicio);
            etFechaFin.setText(fechaFin);

            tvTitulo.setText("Editar Programación");
            btnAccion.setText("Actualizar");
        } else {
            tvTitulo.setText("Crear Programación +");
            btnAccion.setText("Crear");
        }

        // Eventos de fecha/hora
        etFechaInicio.setOnClickListener(v -> mostrarDateTimePicker(etFechaInicio));
        etFechaFin.setOnClickListener(v -> mostrarDateTimePicker(etFechaFin));

        btnCancelar.setOnClickListener(v -> {
            Intent intent = new Intent(FormProIluminacionActivity.this, ProgramacionIluminacionActivity.class);
            intent.putExtra("zona_id", idZona);
            startActivity(intent);
            finish();
        });

        // Botón acción (crear o actualizar según caso)
        btnAccion.setOnClickListener(v -> {
            if (programacionId == -1) {
                crearProgramacion();
            } else {
                actualizarProgramacion();
            }
        });
    }

    private void mostrarDateTimePicker(EditText editText) {
        final Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePicker = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    TimePickerDialog timePicker = new TimePickerDialog(
                            this,
                            (timeView, hourOfDay, minute) -> {
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendar.set(Calendar.MINUTE, minute);

                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                                editText.setText(sdf.format(calendar.getTime()));
                            },
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            true
                    );
                    timePicker.show();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePicker.show();
    }

    private void crearProgramacion() {
        String descripcion = etDescripcion.getText().toString().trim();
        String fechaInicioStr = etFechaInicio.getText().toString().trim();
        String fechaFinStr = etFechaFin.getText().toString().trim();

        if (descripcion.isEmpty() || fechaInicioStr.isEmpty() || fechaFinStr.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime fechaInicio = LocalDateTime.parse(fechaInicioStr, formatter);
            LocalDateTime fechaFin = LocalDateTime.parse(fechaFinStr, formatter);

            ProgramacionIluminacion programacion = new ProgramacionIluminacion();
            programacion.setDescripcion(descripcion);
            programacion.setFecha_inicio(fechaInicio);
            programacion.setFecha_finalizacion(fechaFin);
            programacion.setId_zona(idZona);
            programacion.setEstado(true);

            api.crearProgramacion(programacion).enqueue(new Callback<ProgramacionIluminacion>() {
                @Override
                public void onResponse(Call<ProgramacionIluminacion> call, Response<ProgramacionIluminacion> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(FormProIluminacionActivity.this,
                                "Programación creada correctamente", Toast.LENGTH_SHORT).show();
                        volverALista();
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

        } catch (Exception e) {
            Toast.makeText(this, "Formato de fecha incorrecto", Toast.LENGTH_SHORT).show();
        }
    }

    private void actualizarProgramacion() {
        String descripcion = etDescripcion.getText().toString().trim();
        String fechaInicioStr = etFechaInicio.getText().toString().trim();
        String fechaFinStr = etFechaFin.getText().toString().trim();

        if (descripcion.isEmpty() || fechaInicioStr.isEmpty() || fechaFinStr.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime fechaInicio = LocalDateTime.parse(fechaInicioStr, formatter);
            LocalDateTime fechaFin = LocalDateTime.parse(fechaFinStr, formatter);

            ProgramacionIluminacion programacion = new ProgramacionIluminacion();
            programacion.setDescripcion(descripcion);
            programacion.setFecha_inicio(fechaInicio);
            programacion.setFecha_finalizacion(fechaFin);
            programacion.setId_zona(idZona);
            programacion.setEstado(true);

            api.actualizarProgramacion(programacionId, programacion).enqueue(new Callback<ProgramacionIluminacion>() {
                @Override
                public void onResponse(Call<ProgramacionIluminacion> call, Response<ProgramacionIluminacion> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(FormProIluminacionActivity.this,
                                "Programación actualizada correctamente", Toast.LENGTH_SHORT).show();
                        volverALista();
                    } else {
                        Toast.makeText(FormProIluminacionActivity.this,
                                "Error al actualizar: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ProgramacionIluminacion> call, Throwable t) {
                    Toast.makeText(FormProIluminacionActivity.this,
                            "Fallo de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

        } catch (Exception e) {
            Toast.makeText(this, "Formato de fecha incorrecto", Toast.LENGTH_SHORT).show();
        }
    }

    private void volverALista() {
        Intent intent = new Intent(FormProIluminacionActivity.this, ProgramacionIluminacionActivity.class);
        intent.putExtra("zona_id", idZona);
        startActivity(intent);
        finish();
    }
}
