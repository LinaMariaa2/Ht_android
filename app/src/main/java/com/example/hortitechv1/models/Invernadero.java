package com.example.hortitechv1.models;

public class Invernadero {

    public enum Estado{
        activo, inactivo, mantenimiento
    }
    private int id_invernadero;
    private String nombre;
    private String Descripcion;
    private int zonas_totales;
    private int zonas_activas;
    // private Persona responsable_id

    public int getZonas_activas() {
        return zonas_activas;
    }

    public void setZonas_activas(int zonas_activas) {
        this.zonas_activas = zonas_activas;
    }

    public int getZonas_totales() {
        return zonas_totales;
    }

    public void setZonas_totales(int zonas_totales) {
        this.zonas_totales = zonas_totales;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getId_invernadero() {
        return id_invernadero;
    }

    public void setId_invernadero(int id_invernadero) {
        this.id_invernadero = id_invernadero;
    }
}



