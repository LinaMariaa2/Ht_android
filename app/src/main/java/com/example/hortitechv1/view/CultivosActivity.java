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
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.example.hortitechv1.controllers.CultivoAdapter;
import com.example.hortitechv1.models.Cultivo;
import com.example.hortitechv1.network.ApiClient;
import com.example.hortitechv1.network.ApiCultivos;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CultivosActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    RecyclerView rvCultivos;
    private DrawerLayout drawerLayout;
    private LinearLayout mainContentContainer;
    private static final float END_SCALE = 0.8f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cultivos);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        mainContentContainer = findViewById(R.id.main_content_container);
        NavigationView navigationView = findViewById(R.id.navigation_view);

        setupDrawerAnimation(toolbar);
        navigationView.setNavigationItemSelectedListener(this);
        styleLogoutMenuItem(navigationView.getMenu());

        navigationView.setCheckedItem(R.id.nav_crops);

        rvCultivos = findViewById(R.id.rvCultivos);
        rvCultivos.setLayoutManager(new LinearLayoutManager(this));

        fetchCultivosData();
    }

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
                startActivity(new Intent(CultivosActivity.this, HomeActivity.class));
            } else if (itemId == R.id.nav_greenhouses) {
                startActivity(new Intent(CultivosActivity.this, InvernaderoActivity.class));
            } else if (itemId == R.id.nav_log) {
                startActivity(new Intent(CultivosActivity.this, BitacoraActivity.class));
            } else if (itemId == R.id.nav_settings) {
                startActivity(new Intent(CultivosActivity.this, PerfilActivity.class));
            }
        }, 250);

        if (itemId == R.id.nav_logout) {
            Toast.makeText(this, "Cerrando sesión...", Toast.LENGTH_SHORT).show();
            // Lógica para cerrar sesión
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

    // --- Lógica de la API (igual que antes) ---
    private void fetchCultivosData() {
        ApiCultivos api = ApiClient.getClient().create(ApiCultivos.class);
        Call<List<Cultivo>> call = api.getCultivos();

        call.enqueue(new Callback<List<Cultivo>>() {
            @Override
            public void onResponse(Call<List<Cultivo>> call, Response<List<Cultivo>> response) {
                Log.d("API_RESPONSE", "Código de respuesta: " + response.code());
                if (response.isSuccessful() && response.body() != null) {
                    List<Cultivo> lista = response.body();
                    Log.d("API_RESPONSE", "Respuesta exitosa. Items recibidos: " + lista.size());
                    CultivoAdapter adapter = new CultivoAdapter(CultivosActivity.this, lista);
                    rvCultivos.setAdapter(adapter);
                    if (lista.isEmpty()) {
                        Toast.makeText(CultivosActivity.this, "El servidor no devolvió cultivos.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("API_RESPONSE", "La respuesta no fue exitosa. Código: " + response.code());
                    Toast.makeText(CultivosActivity.this, "Error del servidor: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<Cultivo>> call, Throwable t) {
                Log.e("API_FAIL", "Fallo en la conexión: " + t.getMessage(), t);
                Toast.makeText(CultivosActivity.this, "Fallo en la conexión. Revisa el Logcat.", Toast.LENGTH_LONG).show();
            }
        });
    }
}