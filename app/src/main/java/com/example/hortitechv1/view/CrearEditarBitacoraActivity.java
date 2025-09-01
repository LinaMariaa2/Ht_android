package com.example.hortitechv1.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.hortitechv1.R;
import com.example.hortitechv1.models.Bitacora;
import com.example.hortitechv1.network.ApiClient;
import com.example.hortitechv1.network.ApiBitacora;
import com.google.android.material.textfield.TextInputEditText;
import java.util.Arrays;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrearEditarBitacoraActivity extends AppCompatActivity {

    private boolean isEditMode = false;
    private Bitacora bitacoraActual;
    private ApiBitacora apiService;
    private TextInputEditText etTitulo, etContenido;
    private Spinner spinnerTipoEvento, spinnerImportancia, spinnerInvernadero, spinnerZona, spinnerAutor;
    private Button btnGuardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_editar_bitacora);

        apiService = ApiClient.getClient().create(ApiBitacora.class);
        etTitulo = findViewById(R.id.etTitulo);
        etContenido = findViewById(R.id.etContenido);
        spinnerTipoEvento = findViewById(R.id.spinnerTipoEvento);
        spinnerImportancia = findViewById(R.id.spinnerImportancia);
        spinnerInvernadero = findViewById(R.id.spinnerInvernadero);
        spinnerZona = findViewById(R.id.spinnerZona);
        spinnerAutor = findViewById(R.id.spinnerAutor);
        btnGuardar = findViewById(R.id.btnGuardar);
        TextView tvFormTitle = findViewById(R.id.tvFormTitle);

        if (getIntent().hasExtra("BITACORA_EDITAR")) {
            isEditMode = true;
            bitacoraActual = (Bitacora) getIntent().getSerializableExtra("BITACORA_EDITAR");
            tvFormTitle.setText("Editar Bitácora");
            etTitulo.setText(bitacoraActual.getTitulo());
            etContenido.setText(bitacoraActual.getContenido());
        } else {
            tvFormTitle.setText("Nueva Bitácora");
        }

        cargarSpinnersEstaticos();
        cargarSpinnersDinamicos();
        btnGuardar.setOnClickListener(v -> guardarBitacora());
    }

    private void cargarSpinnersEstaticos() {
        ArrayAdapter<String> tipoEventoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Arrays.asList("riego", "iluminacion", "cultivo", "alerta", "mantenimiento", "hardware", "general"));
        spinnerTipoEvento.setAdapter(tipoEventoAdapter);

        ArrayAdapter<String> importanciaAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Arrays.asList("baja", "media", "alta"));
        spinnerImportancia.setAdapter(importanciaAdapter);

        if(isEditMode) {
            setSpinnerSelectionByValue(spinnerTipoEvento, bitacoraActual.getTipo_evento());
            setSpinnerSelectionByValue(spinnerImportancia, bitacoraActual.getImportancia());
        }
    }

    private void cargarSpinnersDinamicos() {
        apiService.getAutores().enqueue(new Callback<List<Bitacora.Autor>>() {
            @Override
            public void onResponse(Call<List<Bitacora.Autor>> call, Response<List<Bitacora.Autor>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    List<Bitacora.Autor> autores = response.body();
                    ArrayAdapter<Bitacora.Autor> adapter = new ArrayAdapter<>(CrearEditarBitacoraActivity.this, android.R.layout.simple_spinner_item, autores);
                    spinnerAutor.setAdapter(adapter);
                    if(isEditMode) setSpinnerSelectionByAutorId(spinnerAutor, autores, bitacoraActual.getAutor_id());
                }
            }
            @Override public void onFailure(Call<List<Bitacora.Autor>> call, Throwable t) {}
        });

        apiService.getInvernaderos().enqueue(new Callback<List<Bitacora.Invernadero>>() {
            @Override
            public void onResponse(Call<List<Bitacora.Invernadero>> call, Response<List<Bitacora.Invernadero>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    List<Bitacora.Invernadero> invernaderos = response.body();
                    ArrayAdapter<Bitacora.Invernadero> adapter = new ArrayAdapter<>(CrearEditarBitacoraActivity.this, android.R.layout.simple_spinner_item, invernaderos);
                    spinnerInvernadero.setAdapter(adapter);
                    if(isEditMode) setSpinnerSelectionByInvernaderoId(spinnerInvernadero, invernaderos, bitacoraActual.getId_invernadero());
                }
            }
            @Override public void onFailure(Call<List<Bitacora.Invernadero>> call, Throwable t) {}
        });

        spinnerInvernadero.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Bitacora.Invernadero invernadero = (Bitacora.Invernadero) parent.getItemAtPosition(position);
                if (invernadero != null) cargarZonas(invernadero.getId_invernadero());
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void cargarZonas(int idInvernadero) {
        apiService.getZonasPorInvernadero(idInvernadero).enqueue(new Callback<List<Bitacora.Zona>>() {
            @Override
            public void onResponse(Call<List<Bitacora.Zona>> call, Response<List<Bitacora.Zona>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    List<Bitacora.Zona> zonas = response.body();
                    ArrayAdapter<Bitacora.Zona> adapter = new ArrayAdapter<>(CrearEditarBitacoraActivity.this, android.R.layout.simple_spinner_item, zonas);
                    spinnerZona.setAdapter(adapter);
                    if(isEditMode && bitacoraActual.getId_zona() != null) {
                        setSpinnerSelectionByZonaId(spinnerZona, zonas, bitacoraActual.getId_zona());
                    }
                }
            }
            @Override public void onFailure(Call<List<Bitacora.Zona>> call, Throwable t) {}
        });
    }

    private void guardarBitacora() {
        if (etTitulo.getText().toString().isEmpty() || etContenido.getText().toString().isEmpty()) {
            Toast.makeText(this, "Título y contenido son obligatorios.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Se crea o se usa el objeto existente. 'bitacora' es el "carro real".
        Bitacora bitacora = isEditMode ? bitacoraActual : new Bitacora();
        bitacora.setTitulo(etTitulo.getText().toString());
        bitacora.setContenido(etContenido.getText().toString());
        bitacora.setTipo_evento(spinnerTipoEvento.getSelectedItem().toString());
        bitacora.setImportancia(spinnerImportancia.getSelectedItem().toString());

        // --- AQUÍ ESTABA EL ERROR: Se debe llamar a los métodos sobre el objeto 'bitacora' (con minúscula) ---
        if (spinnerInvernadero.getSelectedItem() != null) bitacora.setId_invernadero(((Bitacora.Invernadero) spinnerInvernadero.getSelectedItem()).getId_invernadero());
        if (spinnerZona.getSelectedItem() != null) bitacora.setId_zona(((Bitacora.Zona) spinnerZona.getSelectedItem()).getId_zona());
        if (spinnerAutor.getSelectedItem() != null) bitacora.setAutor_id(((Bitacora.Autor) spinnerAutor.getSelectedItem()).getId_persona());

        Call<ResponseBody> call = isEditMode ?
                apiService.actualizarBitacora(bitacora.getId_publicacion(), bitacora) :
                apiService.crearBitacora(bitacora);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CrearEditarBitacoraActivity.this, "Bitácora guardada", Toast.LENGTH_SHORT).show();
                    setResult(Activity.RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(CrearEditarBitacoraActivity.this, "Error al guardar: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(CrearEditarBitacoraActivity.this, "Fallo de conexión al guardar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // --- Métodos de utilidad para seleccionar en Spinners ---
    private void setSpinnerSelectionByValue(Spinner spinner, String value) {
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinner.getAdapter();
        if (value != null && adapter != null) {
            for (int i = 0; i < adapter.getCount(); i++) {
                if (adapter.getItem(i).equals(value)) { spinner.setSelection(i); break; }
            }
        }
    }
    private void setSpinnerSelectionByAutorId(Spinner spinner, List<Bitacora.Autor> items, int id) {
        for (int i = 0; i < items.size(); i++) if (items.get(i).getId_persona() == id) { spinner.setSelection(i); break; }
    }
    private void setSpinnerSelectionByInvernaderoId(Spinner spinner, List<Bitacora.Invernadero> items, int id) {
        for (int i = 0; i < items.size(); i++) if (items.get(i).getId_invernadero() == id) { spinner.setSelection(i); break; }
    }
    private void setSpinnerSelectionByZonaId(Spinner spinner, List<Bitacora.Zona> items, int id) {
        for (int i = 0; i < items.size(); i++) if (items.get(i).getId_zona() == id) { spinner.setSelection(i); break; }
    }
}