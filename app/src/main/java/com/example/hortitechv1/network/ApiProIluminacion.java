package com.example.hortitechv1.network;

import com.example.hortitechv1.models.ProgramacionIluminacion;
import com.example.hortitechv1.models.Zona;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.*;

public interface ApiProIluminacion {

        @GET("api/programacioniluminacion")
        Call<List<ProgramacionIluminacion>> getTodasLasProgramaciones();

        @GET("api/programacioniluminacion/zona/{idZona}/futuras")
        Call<List<ProgramacionIluminacion>> getProgramacionesFuturas(@Path("idZona") int idZona);

        @GET("api/programacioniluminacion/{id}")
        Call<ProgramacionIluminacion> getProgramacionPorId(@Path("id") int id);

        @POST("api/programacioniluminacion")
        Call<ProgramacionIluminacion> crearProgramacion(@Body ProgramacionIluminacion programacion);

        @PUT("api/programacioniluminacion/{id}")
        Call<ProgramacionIluminacion> actualizarProgramacion(
                @Path("id") int id,
                @Body ProgramacionIluminacion programacion
        );

        @DELETE("api/programacioniluminacion/{id}")
        Call<Void> eliminarProgramacion(@Path("id") int id);

}

