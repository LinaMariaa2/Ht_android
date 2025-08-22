package com.example.hortitechv1.models;

import java.time.LocalDateTime;

public class ProgramacionRiego {
    private Integer id_pg_riego;
    private String descripcion;
    private String tipo_riego;
    private LocalDateTime fecha_inicio;
    private LocalDateTime fecha_finalizacion;
    private int id_zona;
    private boolean estado;

    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public ProgramacionRiego() {}

    public ProgramacionRiego(Integer id_pg_riego, String descripcion, String tipo_riego,
                             LocalDateTime fecha_inicio, LocalDateTime fecha_finalizacion,
                             LocalDateTime created_at, LocalDateTime updated_at,
                             int id_zona, boolean estado) {
        this.id_pg_riego = id_pg_riego;
        this.descripcion = descripcion;
        setTipo_riego(tipo_riego); // 👉 usamos el setter para validar
        this.fecha_inicio = fecha_inicio;
        this.fecha_finalizacion = fecha_finalizacion;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.id_zona = id_zona;
        this.estado = estado;
    }

    public Integer getId_pg_riego() {
        return id_pg_riego;
    }

    public void setId_pg_riego(Integer id_pg_riego) {
        this.id_pg_riego = id_pg_riego;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipo_riego() {
        return tipo_riego;
    }


    public void setTipo_riego(String tipo_riego) {
        if (tipo_riego == null) {
            this.tipo_riego = null;
        } else {
            String lower = tipo_riego.toLowerCase();
            if (lower.equals("goteo") || lower.equals("aspersión") || lower.equals("manual")) {
                this.tipo_riego = lower;
            } else {
                throw new IllegalArgumentException(
                        "Valor inválido para tipo_riego: " + tipo_riego +
                                ". Solo se permiten: goteo, aspersión, manual."
                );
            }
        }
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

    public int getId_zona() {
        return id_zona;
    }

    public void setId_zona(int id_zona) {
        this.id_zona = id_zona;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
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
}
