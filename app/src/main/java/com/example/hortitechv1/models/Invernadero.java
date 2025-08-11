package com.example.hortitechv1.models;

public class Invernadero {

    // Propiedades
    private int id_invernadero;
    private String nombre;
    private String descripcion;
    private Estado estado;
    private int zonas_totales;
    private int zonas_activas;
    private int responsable_id;
    private Persona encargado;

    // Constructor vacío
    public Invernadero() {
    }

    // Constructor con parámetros
    public Invernadero(int id_invernadero, String nombre, String descripcion, Estado estado, int zonas_totales, int zonas_activas,int responsable_id, Persona encargado) {
        this.id_invernadero = id_invernadero;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.estado = estado;
        this.zonas_totales = zonas_totales;
        this.zonas_activas = zonas_activas;
        this.responsable_id = responsable_id;
        this.encargado = encargado;
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

    public int getResponsable_id() {
        return responsable_id;
    }

    public void setResponsable_id(int responsable_id) {
        this.responsable_id = responsable_id;
    }

    public Persona getEncargado() {
        return encargado;
    }

    public void setEncargado(Persona encargado) {
        this.encargado = encargado;
    }
}

