package com.example.hortitechv1.models;

import java.time.LocalDateTime;

public class ProgramacionIluminacion {
    private Integer id_iluminacion;
    private int id_zona;
    private String descripcion;
    private LocalDateTime fecha_inicio;
    private LocalDateTime fecha_finalizacion;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private boolean estado;

    //Constructor vacio
    public ProgramacionIluminacion(){
    }

    public ProgramacionIluminacion(Integer id_iluminacion, int id_zona, String descripcion, LocalDateTime fecha_inicio, LocalDateTime fecha_finalizacion, LocalDateTime created_at, LocalDateTime updated_at, boolean estado){
        this.id_iluminacion = id_iluminacion;
        this.id_zona = id_zona;
        this.descripcion = descripcion;
        this.fecha_inicio = fecha_inicio;
        this.fecha_finalizacion= fecha_finalizacion;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.estado = estado;
    }

    public Integer getId_iluminacion() {
        return id_iluminacion;
    }

    public void setId_iluminacion(Integer id_iluminacion) {
        this.id_iluminacion = id_iluminacion;
    }

    public int getId_zona() {
        return id_zona;
    }

    public void setId_zona(int id_zona) {
        this.id_zona = id_zona;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(LocalDateTime fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public LocalDateTime getFecha_finalizacion() {
        return fecha_finalizacion;
    }

    public void setFecha_finalizacion(LocalDateTime fecha_finalizacion) {
        this.fecha_finalizacion = fecha_finalizacion;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public LocalDateTime getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(LocalDateTime updated_at) {
        this.updated_at = updated_at;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
}
