package com.example.hortitechv1.view;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.example.hortitechv1.R;
import com.example.hortitechv1.controllers.SessionManager;
import com.google.android.material.navigation.NavigationView;

public class PerfilActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private TextView tvNombreUsuario, tvCorreo, tvRol;
    private ImageView ivFotoPerfil;
    private SessionManager sessionManager;
    private DrawerLayout drawerLayout;
    private LinearLayout mainContentContainer;
    private static final float END_SCALE = 0.8f;

    private final ActivityResultLauncher<Intent> editProfileLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    mostrarDatosGuardados();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        mainContentContainer = findViewById(R.id.main_content_container);
        NavigationView navigationView = findViewById(R.id.navigation_view);

        setupDrawerAnimation(toolbar);
        navigationView.setNavigationItemSelectedListener(this);
        styleLogoutMenuItem(navigationView.getMenu());
        navigationView.setCheckedItem(R.id.nav_settings);

        sessionManager = new SessionManager(this);

        tvNombreUsuario = findViewById(R.id.tvNombreUsuario);
        tvCorreo = findViewById(R.id.tvCorreo);
        tvRol = findViewById(R.id.tvRol);
        ivFotoPerfil = findViewById(R.id.ivFotoPerfil);
        Button btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        Button btnEditarPerfil = findViewById(R.id.btnEditarPerfil);

        mostrarDatosGuardados();

        btnCerrarSesion.setOnClickListener(v -> sessionManager.logoutUser());

        btnEditarPerfil.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditarPerfilActivity.class);
            editProfileLauncher.launch(intent);
        });
    }

    private void mostrarDatosGuardados() {
        String nombre = sessionManager.getUserName();
        String correo = sessionManager.getUserEmail();
        String rol = sessionManager.getUserRol();
        String fotoUrl = sessionManager.getUserFotoUrl();

        if (nombre != null) {
            tvNombreUsuario.setText(nombre);
            tvCorreo.setText(correo);
            tvRol.setText("Rol: " + rol);

            if (fotoUrl != null && !fotoUrl.isEmpty()) {
                Glide.with(this)
                        .load(fotoUrl)
                        .circleCrop()
                        .placeholder(R.drawable.ic_profile_placeholder)
                        .into(ivFotoPerfil);
            }
        } else {
            Toast.makeText(this, "No se pudo cargar la sesión. Por favor, inicia sesión de nuevo.", Toast.LENGTH_LONG).show();
            sessionManager.logoutUser();
        }
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
                startActivity(new Intent(PerfilActivity.this, HomeActivity.class));
            } else if (itemId == R.id.nav_greenhouses) {
                startActivity(new Intent(PerfilActivity.this, InvernaderoActivity.class));
            } else if (itemId == R.id.nav_crops) {
                startActivity(new Intent(PerfilActivity.this, CultivosActivity.class));
            } else if (itemId == R.id.nav_log) {
                startActivity(new Intent(PerfilActivity.this, BitacoraActivity.class));
            }
        }, 250);

        if (itemId == R.id.nav_logout) {
            Toast.makeText(this, "Cerrando sesión...", Toast.LENGTH_SHORT).show();
            sessionManager.logoutUser();
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