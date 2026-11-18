
package com.udh.apphuellitas.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "dietas")
public class Dieta {

    @PrimaryKey
    @NonNull
    private String idDieta = "";

    private String idMascota;
    private String marcaAlimento;
    private double racionGramos;
    private int frecuenciaDiaria;

    public Dieta() {
    }

    @NonNull
    public String getIdDieta() {
        return idDieta;
    }

    public void setIdDieta(@NonNull String idDieta) {
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

    public double getRacionGramos() {
        return racionGramos;
    }

    public void setRacionGramos(double racionGramos) {
        this.racionGramos = racionGramos;
    }

    public int getFrecuenciaDiaria() {
        return frecuenciaDiaria;
    }

    public void setFrecuenciaDiaria(int frecuenciaDiaria) {
        this.frecuenciaDiaria = frecuenciaDiaria;
    }
}
