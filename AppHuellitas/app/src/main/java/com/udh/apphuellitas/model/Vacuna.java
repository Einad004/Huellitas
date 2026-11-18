
package com.udh.apphuellitas.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "vacunas")
public class Vacuna {

    @PrimaryKey
    @NonNull
    private String idVacuna = "";

    private String idMascota;
    private String nombreVacuna;
    private String lote;
    private String fechaAplicacion;

    public Vacuna() {
    }

    @NonNull
    public String getIdVacuna() {
        return idVacuna;
    }

    public void setIdVacuna(@NonNull String idVacuna) {
        this.idVacuna = idVacuna;
    }

    public String getIdMascota() {
        return idMascota;
    }

    public void setIdMascota(String idMascota) {
        this.idMascota = idMascota;
    }

    public String getNombreVacuna() {
        return nombreVacuna;
    }

    public void setNombreVacuna(String nombreVacuna) {
        this.nombreVacuna = nombreVacuna;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public String getFechaAplicacion() {
        return fechaAplicacion;
    }

    public void setFechaAplicacion(String fechaAplicacion) {
        this.fechaAplicacion = fechaAplicacion;
    }
}
