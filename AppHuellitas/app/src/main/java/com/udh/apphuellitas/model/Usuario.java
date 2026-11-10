package com.udh.apphuellitas.model;

public class Usuario {
    private String idUsuario;
    private String correo;
    private String nombre;

    public Usuario() {}

    public Usuario(String idUsuario, String correo, String nombre) {
        this.idUsuario = idUsuario;
        this.correo = correo;
        this.nombre = nombre;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
