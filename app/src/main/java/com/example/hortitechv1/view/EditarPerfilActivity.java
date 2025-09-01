package com.example.hortitechv1.view;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.hortitechv1.R;
import com.example.hortitechv1.models.LoginResponse;
import com.example.hortitechv1.models.Persona;
import com.example.hortitechv1.network.ApiClient;
import com.example.hortitechv1.network.ApiUsuario;
import com.example.hortitechv1.controllers.SessionManager;
import com.google.android.material.textfield.TextInputEditText;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditarPerfilActivity extends AppCompatActivity {
    private TextInputEditText etNombreUsuario, etCorreo, etContrasena;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);
        sessionManager = new SessionManager(this);

        etNombreUsuario = findViewById(R.id.etNombreUsuario);
        etCorreo = findViewById(R.id.etCorreo);
        etContrasena = findViewById(R.id.etContrasena);
        Button btnGuardarCambios = findViewById(R.id.btnGuardarCambios);

        etNombreUsuario.setText(sessionManager.getUserName());
        etCorreo.setText(sessionManager.getUserEmail());

        btnGuardarCambios.setOnClickListener(v -> guardarCambios());
    }

    private void guardarCambios() {
        String nombre = etNombreUsuario.getText().toString().trim();
        String correo = etCorreo.getText().toString().trim();
        String contrasena = etContrasena.getText().toString().trim();

        Persona personaActualizada = new Persona();
        personaActualizada.setNombre_usuario(nombre);
        personaActualizada.setCorreo(correo);
        if (!contrasena.isEmpty()) {
            personaActualizada.setContrasena(contrasena);
        }

        ApiUsuario api = ApiClient.getClient().create(ApiUsuario.class);
        api.updateAuthenticatedUserProfile(sessionManager.getAuthToken(), personaActualizada).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // Actualizar los datos guardados en SharedPreferences localmente
                    // (La API no devuelve el usuario actualizado, así que lo hacemos manual)
                    Persona user = new Persona();
                    user.setId_persona(sessionManager.getUserId());
                    user.setNombre_usuario(nombre);
                    user.setCorreo(correo);

                    Toast.makeText(EditarPerfilActivity.this, "Perfil actualizado", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(EditarPerfilActivity.this, "Error al actualizar", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(EditarPerfilActivity.this, "Fallo de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}