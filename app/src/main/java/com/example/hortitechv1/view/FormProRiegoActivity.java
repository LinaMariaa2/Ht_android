package com.example.hortitechv1.view;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hortitechv1.R;
import com.example.hortitechv1.models.ProgramacionRiego;
import com.example.hortitechv1.network.ApiClient;
import com.example.hortitechv1.network.ApiProRiego;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormProRiegoActivity extends AppCompatActivity {

    private EditText etDescripcion, etFechaInicio, etFechaFin;
    private Spinner spinnerTipoRiego;
    private Button btnAccion, btnCancelar;
    private TextView tvTitulo;
    private ApiProRiego api;
    private int idZona;
    private int programacionId = -1;


    private final String[] tiposRiego = {"aspersión", "goteo", "manual"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_pro_riego);

        // Inicializar vistas
        etDescripcion = findViewById(R.id.etPR);
        etFechaInicio = findViewById(R.id.etFechaInicioR);
        etFechaFin = findViewById(R.id.etFechaFinR);
        spinnerTipoRiego = findViewById(R.id.spinnerTipoRiego);
        btnAccion = findViewById(R.id.btnCrearR);
        btnCancelar = findViewById(R.id.btnCancelarFormR);
        tvTitulo = findViewById(R.id.tvTituloFormR);

        // Inicializar API
        api = ApiClient.getClient().create(ApiProRiego.class);

        // Configurar Spinner con los valores de tipo de riego
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                tiposRiego
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipoRiego.setAdapter(adapter);

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
            String descripcion = getIntent().getStringExtra("descripcion");
            String fechaInicio = getIntent().getStringExtra("fecha_inicio");
            String fechaFin = getIntent().getStringExtra("fecha_fin");
            String tipoRiego = getIntent().getStringExtra("tipo_riego");

            etDescripcion.setText(descripcion);
            etFechaInicio.setText(fechaInicio);
            etFechaFin.setText(fechaFin);

            if (tipoRiego != null) {
                int pos = adapter.getPosition(tipoRiego);
                if (pos >= 0) spinnerTipoRiego.setSelection(pos);
            }

            tvTitulo.setText("Editar Programación");
            btnAccion.setText("Actualizar");
        } else {
            tvTitulo.setText("Crear Programación");
            btnAccion.setText("Crear");
        }

        // Eventos de fecha/hora
        etFechaInicio.setOnClickListener(v -> mostrarDateTimePicker(etFechaInicio));
        etFechaFin.setOnClickListener(v -> mostrarDateTimePicker(etFechaFin));


        btnCancelar.setOnClickListener(v -> {
            Intent intent = new Intent(FormProRiegoActivity.this, ProgramacionRiegoActivity.class);
            intent.putExtra("zona_id", idZona);
            startActivity(intent);
            finish();
        });

        // Botón acción
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
        String tipoSeleccionado = spinnerTipoRiego.getSelectedItem().toString();

        if (descripcion.isEmpty() || fechaInicioStr.isEmpty() || fechaFinStr.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime fechaInicio = LocalDateTime.parse(fechaInicioStr, formatter);
            LocalDateTime fechaFin = LocalDateTime.parse(fechaFinStr, formatter);

            ProgramacionRiego programacion = new ProgramacionRiego();
            programacion.setDescripcion(descripcion);
            programacion.setFecha_inicio(fechaInicio);
            programacion.setFecha_finalizacion(fechaFin);
            programacion.setTipo_riego(tipoSeleccionado);
            programacion.setId_zona(idZona);
            programacion.setEstado(true);

            api.crearProgramacion(programacion).enqueue(new Callback<ProgramacionRiego>() {
                @Override
                public void onResponse(Call<ProgramacionRiego> call, Response<ProgramacionRiego> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(FormProRiegoActivity.this,
                                "Programación de riego creada correctamente", Toast.LENGTH_SHORT).show();
                        volverALista();
                    } else {
                        Toast.makeText(FormProRiegoActivity.this,
                                "Error al crear: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ProgramacionRiego> call, Throwable t) {
                    Toast.makeText(FormProRiegoActivity.this,
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
        String tipoSeleccionado = spinnerTipoRiego.getSelectedItem().toString();

        if (descripcion.isEmpty() || fechaInicioStr.isEmpty() || fechaFinStr.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime fechaInicio = LocalDateTime.parse(fechaInicioStr, formatter);
            LocalDateTime fechaFin = LocalDateTime.parse(fechaFinStr, formatter);

            ProgramacionRiego programacion = new ProgramacionRiego();
            programacion.setDescripcion(descripcion);
            programacion.setFecha_inicio(fechaInicio);
            programacion.setFecha_finalizacion(fechaFin);
            programacion.setTipo_riego(tipoSeleccionado);
            programacion.setId_zona(idZona);
            programacion.setEstado(true);

            api.actualizarProgramacion(programacionId, programacion).enqueue(new Callback<ProgramacionRiego>() {
                @Override
                public void onResponse(Call<ProgramacionRiego> call, Response<ProgramacionRiego> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(FormProRiegoActivity.this,
                                "Programación de riego actualizada correctamente", Toast.LENGTH_SHORT).show();
                        volverALista();
                    } else {
                        Toast.makeText(FormProRiegoActivity.this,
                                "Error al actualizar: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ProgramacionRiego> call, Throwable t) {
                    Toast.makeText(FormProRiegoActivity.this,
                            "Fallo de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

        } catch (Exception e) {
            Toast.makeText(this, "Formato de fecha incorrecto", Toast.LENGTH_SHORT).show();
        }
    }

    private void volverALista() {
        Intent intent = new Intent(FormProRiegoActivity.this, ProgramacionRiegoActivity.class);
        intent.putExtra("zona_id", idZona);
        startActivity(intent);
        finish();
    }
}
