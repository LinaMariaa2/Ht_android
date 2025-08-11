package com.example.hortitechv1.network;

import com.example.hortitechv1.models.ProgramacionRiego;
import com.example.hortitechv1.models.Zona;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.*;

public interface ApiProRiego {
    //Listamos todas las programaciones
    @GET("api/programacionRiego")
    Call<List<ProgramacionRiego>> getProgramaciones();

    // Obtener zonas activas para el ESP32
    @GET("api/programacionRiego/zonas/activas")
    Call<List<Zona>> getZonasRiegoActivas();

    @GET("api/programacionRiego/{id}")
    Call<ProgramacionRiego> getProgramacionPorId(@Path("id") int id);

    @POST("api/programacionRiego")
    Call<ProgramacionRiego> crearProgramacion(@Body ProgramacionRiego programacion);

    @PUT("api/programacionRiego/{id}")
    Call<ProgramacionRiego> actualizarProgramacion(
            @Path("id") int id,
            @Body ProgramacionRiego programacion
    );

    @DELETE("api/programacionRiego/{id}")
    Call<Void> eliminarProgramacion(@Path("id") int id);
}

