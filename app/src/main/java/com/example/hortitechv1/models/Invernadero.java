package com.example.hortitechv1.models;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.Date;

public class Invernadero implements Serializable {
    @SerializedName("id_invernadero")
    private int id_invernadero;
    private String nombre;
    private String descripcion;
    private Estado estado;
    @SerializedName("zonas_totales")
    private int zonas_totales;
    @SerializedName("zonas_activas")
    private int zonas_activas;
    @SerializedName("responsable_id")
    private int responsable_id;
    private Date createdAt;
    private Date updatedAt;
    // Si tu JSON incluye un objeto "encargado" de tipo Persona, deberás añadirlo aquí:
    // private Persona encargado;

    public Invernadero() {}

    public int getId_invernadero() { return id_invernadero; }
    public void setId_invernadero(int id_invernadero) { this.id_invernadero = id_invernadero; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }
    public int getZonas_totales() { return zonas_totales; }
    public void setZonas_totales(int zonas_totales) { this.zonas_totales = zonas_totales; }
    public int getZonas_activas() { return zonas_activas; }
    public void setZonas_activas(int zonas_activas) { this.zonas_activas = zonas_activas; }
    public int getResponsable_id() { return responsable_id; }
    public void setResponsable_id(int responsable_id) { this.responsable_id = responsable_id; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
}