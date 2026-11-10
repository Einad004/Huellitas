package com.udh.apphuellitas.model;

public class Dieta {
    private String idDieta;
    private String idMascota;
    private String marcaAlimento;
    private int racionGramos;
    private int frecuenciaDiaria;

    public Dieta() { }

    public Dieta(String idDieta, String idMascota, String marcaAlimento, int racionGramos, int frecuenciaDiaria) {
        this.idDieta = idDieta;
        this.idMascota = idMascota;
        this.marcaAlimento = marcaAlimento;
        this.racionGramos = racionGramos;
        this.frecuenciaDiaria = frecuenciaDiaria;
    }

    public String getIdDieta() {
        return idDieta;
    }

    public void setIdDieta(String idDieta) {
        this.idDieta = idDieta;
    }

    public String getIdMascota() {
        return idMascota;
    }

    public void setIdMascota(String idMascota) {
        this.idMascota = idMascota;
    }

    public String getMarcaAlimento() {
        return marcaAlimento;
    }

    public void setMarcaAlimento(String marcaAlimento) {
        this.marcaAlimento = marcaAlimento;
    }

    public int getRacionGramos() {
        return racionGramos;
    }

    public void setRacionGramos(int racionGramos) {
        this.racionGramos = racionGramos;
    }

    public int getFrecuenciaDiaria() {
        return frecuenciaDiaria;
    }

    public void setFrecuenciaDiaria(int frecuenciaDiaria) {
        this.frecuenciaDiaria = frecuenciaDiaria;
    }
}
