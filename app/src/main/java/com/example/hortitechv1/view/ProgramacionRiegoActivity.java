package com.example.hortitechv1.view;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hortitechv1.R;
import com.example.hortitechv1.controllers.ProgramacionRiegoAdapter;
import com.example.hortitechv1.models.ProgramacionRiego;
import com.example.hortitechv1.network.ApiClient;
import com.example.hortitechv1.network.ApiProRiego;
import com.google.android.material.navigation.NavigationView;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProgramacionRiegoActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerView;
    private Button btnNueva;
    private ProgramacionRiegoAdapter adapter;
    private List<ProgramacionRiego> listaProgramaciones = new ArrayList<>();
    private ApiProRiego api;
    private int idZona;

    private DrawerLayout drawerLayout;
    private LinearLayout mainContentContainer;
    private static final float END_SCALE = 0.8f;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programacion_riego);

        // Configuración del Toolbar y DrawerLayout
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        mainContentContainer = findViewById(R.id.main_content_container);
        NavigationView navigationView = findViewById(R.id.navigation_view);

        setupDrawerAnimation(toolbar);
        navigationView.setNavigationItemSelectedListener(this);
        styleLogoutMenuItem(navigationView.getMenu());

        // Asegúrate de que el elemento correcto del menú está seleccionado
        navigationView.setCheckedItem(R.id.nav_greenhouses);

        // Views
        recyclerView = findViewById(R.id.rvProgramacionRiego);
        btnNueva = findViewById(R.id.btnNuevaProgramacionRiego);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // API
        api = ApiClient.getClient().create(ApiProRiego.class);

        // Adapter con listeners
        adapter = new ProgramacionRiegoAdapter(
                this,
                listaProgramaciones,
                new ProgramacionRiegoAdapter.OnItemClickListener() {
                    @Override
                    public void onActualizarClick(ProgramacionRiego programacion) {
                        actualizarProgramacion(programacion);
                    }

                    @Override
                    public void onDetenerClick(ProgramacionRiego programacion) {
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
            Intent intent = new Intent(ProgramacionRiegoActivity.this, FormProRiegoActivity.class);
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
        api.getProgramacionesFuturas(idZona).enqueue(new Callback<List<ProgramacionRiego>>() {
            @Override
            public void onResponse(Call<List<ProgramacionRiego>> call, Response<List<ProgramacionRiego>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ProgramacionRiego> soloDeZona = new ArrayList<>();
                    for (ProgramacionRiego p : response.body()) {
                        if (p.getId_zona() == idZona) soloDeZona.add(p);
                    }
                    listaProgramaciones.clear();
                    listaProgramaciones.addAll(soloDeZona);
                    adapter.notifyDataSetChanged();
                    Log.d("API_RESPONSE", "Programaciones recibidas (filtradas): " + soloDeZona.size());
                } else {
                    try {
                        String errorMsg = response.errorBody() != null ? response.errorBody().string() : "sin cuerpo";
                        Log.e("API_RESPONSE", "Error: " + response.code() + " -> " + errorMsg);
                        Toast.makeText(ProgramacionRiegoActivity.this, "Error: " + errorMsg, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Log.e("API_RESPONSE", "No se pudo leer errorBody", e);
                    }

                    Toast.makeText(ProgramacionRiegoActivity.this,
                            "Error del servidor: " + response.code(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ProgramacionRiego>> call, Throwable t) {
                Toast.makeText(ProgramacionRiegoActivity.this,
                        "Fallo de conexión: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
                Log.e("API_RESPONSE", "Fallo: ", t);
            }
        });
    }

    private void detenerProgramacion(ProgramacionRiego p) {
        api.eliminarProgramacion(p.getId_pg_riego()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ProgramacionRiegoActivity.this,
                            "Programación eliminada",
                            Toast.LENGTH_SHORT).show();
                    getProgramacionesFuturas();
                } else {
                    Toast.makeText(ProgramacionRiegoActivity.this,
                            "No se pudo eliminar",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ProgramacionRiegoActivity.this,
                        "Error: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void actualizarProgramacion(ProgramacionRiego p) {
        Intent intent = new Intent(this, FormProRiegoActivity.class);
        intent.putExtra("zona_id", idZona);
        intent.putExtra("programacion_id", p.getId_pg_riego());
        intent.putExtra("descripcion", p.getDescripcion());
        intent.putExtra("tipo_riego", p.getTipo_riego());

        // Formato de fechas para el form
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        intent.putExtra("fecha_inicio", p.getFecha_inicio().format(formatter));
        intent.putExtra("fecha_fin", p.getFecha_finalizacion().format(formatter));

        startActivity(intent);
    }
    // --- Lógica del DrawerLayout ---
    private void setupDrawerAnimation(Toolbar toolbar) {
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                final float scale = 1 - (1 - END_SCALE) * slideOffset;
                mainContentContainer.setScaleX(scale);
                mainContentContainer.setScaleY(scale);
                final float xOffset = drawerView.getWidth() * slideOffset;
                mainContentContainer.setTranslationX(xOffset);
            }
        };
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        new android.os.Handler().postDelayed(() -> {
            if (itemId == R.id.nav_home) {
                startActivity(new Intent(ProgramacionRiegoActivity.this, HomeActivity.class));
            } else if (itemId == R.id.nav_greenhouses) {
                startActivity(new Intent(ProgramacionRiegoActivity.this, InvernaderoActivity.class));
            } else if (itemId == R.id.nav_crops) {
                startActivity(new Intent(ProgramacionRiegoActivity.this, CultivosActivity.class));
            } else if (itemId == R.id.nav_log) {
                startActivity(new Intent(ProgramacionRiegoActivity.this, BitacoraActivity.class));
            } else if (itemId == R.id.nav_settings) {
                startActivity(new Intent(ProgramacionRiegoActivity.this, PerfilActivity.class));
            }
        }, 250);

        if (itemId == R.id.nav_logout) {
            Toast.makeText(this, "Cerrando sesión...", Toast.LENGTH_SHORT).show();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void styleLogoutMenuItem(Menu menu) {
        MenuItem logoutItem = menu.findItem(R.id.nav_logout);
        if (logoutItem != null) {
            SpannableString s = new SpannableString(logoutItem.getTitle());
            s.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorError)), 0, s.length(), 0);
            logoutItem.setTitle(s);
            Drawable icon = logoutItem.getIcon();
            if (icon != null) {
                Drawable wrappedIcon = DrawableCompat.wrap(icon);
                DrawableCompat.setTint(wrappedIcon, ContextCompat.getColor(this, R.color.colorError));
                logoutItem.setIcon(wrappedIcon);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}