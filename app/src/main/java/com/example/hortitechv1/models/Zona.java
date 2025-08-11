package com.example.hortitechv1.models;

public class Zona {
    private int id_zona;
    private String nombre;
    private String descripciones_add;
    private Estado estado;
    private int id_invernadero;

    public Zona(){

    }
    public Zona(int id_zona, String nombre, String descripciones_add, Estado estado, int id_invernadero) {
        this.id_zona = id_zona;
        this.nombre = nombre;
        this.descripciones_add = descripciones_add;
        this.estado = estado;
        this.id_invernadero = id_invernadero;
    }

    public int getId_zona() {
        return id_zona;
    }

    public void setId_zona(int id_zona) {
        this.id_zona = id_zona;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripciones_add() {
        return descripciones_add;
    }

    public void setDescripciones_add(String descripciones_add) {
        this.descripciones_add = descripciones_add;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public int getId_invernadero() {
        return id_invernadero;
    }

}
