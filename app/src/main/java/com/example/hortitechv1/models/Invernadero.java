package com.example.hortitechv1.models;
import com.example.hortitechv1.models.Estado;

public class Invernadero {

    // Propiedades del com.linaortega.metodosinvernadero.model.Invernadero
    private int id_invernadero;
    private String nombre;
    private String descripcion;
    private Estado estado;
    private int zonas_totales;
    private int zonas_activas;

    // Constructor vacío
    public Invernadero() {
    }

    // Constructor con parámetros
    public Invernadero(int id_invernadero, String nombre, String descripcion, Estado estado, int zonas_totales, int zonas_activas) {
        this.id_invernadero = id_invernadero;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.estado = estado;
        this.zonas_totales = zonas_totales;
        this.zonas_activas = zonas_activas;
    }

    public int getId_invernadero() {
        return id_invernadero;
    }

    public void setId_invernadero(int id_invernadero) {
        this.id_invernadero = id_invernadero;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public int getZonas_totales() {
        return zonas_totales;
    }

    public void setZonas_totales(int zonas_totales) {
        this.zonas_totales = zonas_totales;
    }

    public int getZonas_activas() {
        return zonas_activas;
    }

    public void setZonas_activas(int zonas_activas) {
        this.zonas_activas = zonas_activas;
    }

}

