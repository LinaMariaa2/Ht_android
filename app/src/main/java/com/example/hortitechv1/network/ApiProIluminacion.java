package com.example.hortitechv1.network;

import com.example.hortitechv1.models.ProgramacionIluminacion;
import com.example.hortitechv1.models.Zona;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.*;

public interface ApiProIluminacion {
    @GET("api/programacionIluminacion/zonas/activas")
    Call<List<Zona>> getZonasActivasParaESP32();

    @GET("api/programacionIluminacion")
    Call<List<ProgramacionIluminacion>> getTodasLasProgramaciones();

    @GET("api/programacionIluminacion/{id}")
    Call<ProgramacionIluminacion> getProgramacionPorId(@Path("id") int id);

    @POST("api/programacionIluminacion")
    Call<ProgramacionIluminacion> crearProgramacion(@Body ProgramacionIluminacion programacion);

    @PUT("api/programacionIluminacion/{id}")
    Call<ProgramacionIluminacion> actualizarProgramacion(
            @Path("id") int id,
            @Body ProgramacionIluminacion programacion
    );

    @DELETE("api/programacionIluminacion/{id}")
    Call<Void> eliminarProgramacion(@Path("id") int id);
}

