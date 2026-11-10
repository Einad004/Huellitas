package com.udh.apphuellitas.model;

import java.util.Date;

public class Vacuna {
    private String idVacuna;
    private String idMascota;
    private String nombreVacuna;
    private String lote;
    private Date fechaAplicacion;

    public Vacuna() {}

    public Vacuna(String idVacuna, String idMascota, String nombreVacuna, String lote, Date fechaAplicacion) {
        this.idVacuna = idVacuna;
        this.idMascota = idMascota;
        this.nombreVacuna = nombreVacuna;
        this.lote = lote;
        this.fechaAplicacion = fechaAplicacion;
    }

    public String getIdVacuna() {
        return idVacuna;
    }

    public void setIdVacuna(String idVacuna) {
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

    public Date getFechaAplicacion() {
        return fechaAplicacion;
    }

    public void setFechaAplicacion(Date fechaAplicacion) {
        this.fechaAplicacion = fechaAplicacion;
    }
}
