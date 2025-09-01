package com.example.hortitechv1.models;

import java.io.Serializable;

public class Persona implements Serializable {
    private int id_persona;
    private String nombre_usuario;
    private String correo;
    private String contrasena;
    private Rol rol; //tambien podriamos dejarlo predefinido porque es solo para operarios
    private Estado estado;
    private boolean isVerified;

    //para la actualizacion de contrasena revisar campos que envia el back!
    private String verificationCode;
    //private time verificationCodeExpires;
    private int intentos;
    private Perfil perfil;


    public Persona() {
    }
    @Override
    public String toString() {
        return nombre_usuario;
    }

    public Persona(int id_persona,String nombre_usuario, String correo, String contrasena, Rol rol, Estado estado,boolean isVerified,String verificationCode, int intentos ) {
        this.id_persona = id_persona;
        this.nombre_usuario = nombre_usuario;
        this.correo = correo;
        this.contrasena = contrasena;
        this.rol = rol;
        this.estado = estado;
        this.isVerified = isVerified;
        this.verificationCode = verificationCode;
        this.intentos = intentos;
    }

    public int getId_persona() {
        return id_persona;
    }

    public void setId_persona(int id_persona) {
        this.id_persona = id_persona;
    }

    public String getNombre_usuario() {
        return nombre_usuario;
    }

    public void setNombre_usuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public int getIntentos() {
        return intentos;
    }

    public void setIntentos(int intentos) {
        this.intentos = intentos;
    }
    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }
}