package com.udh.apphuellitas.model;

import java.util.Date;

public class RegistroFisico {
    private String idRegistro;
    private String idMascota;
    private Date fecha;
    private double peso;
    private double altura;

    public RegistroFisico(String idRegistro, String idMascota, Date fecha, double peso, double altura) {
        this.idRegistro = idRegistro;
        this.idMascota = idMascota;
        this.fecha = fecha;
        this.peso = peso;
        this.altura = altura;
    }

    public String getIdRegistro() {
        return idRegistro;
    }

    public void setIdRegistro(String idRegistro) {
        this.idRegistro = idRegistro;
    }

    public String getIdMascota() {
        return idMascota;
    }

    public void setIdMascota(String idMascota) {
        this.idMascota = idMascota;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public double getAltura() {
        return altura;
    }

    public void setAltura(double altura) {
        this.altura = altura;
    }
}
