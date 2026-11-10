package com.udh.apphuellitas.model;

import java.util.Date;

public class VisitaVeterinaria {
    private String idVisita;
    private String idMascota;
    private Date fecha;
    private String lugar;
    private String motivo;
    private String diagnostico;

    public VisitaVeterinaria() {}

    public VisitaVeterinaria(String idVisita, String idMascota, Date fecha, String lugar, String motivo, String diagnostico) {
        this.idVisita = idVisita;
        this.idMascota = idMascota;
        this.fecha = fecha;
        this.lugar = lugar;
        this.motivo = motivo;
        this.diagnostico = diagnostico;
    }

    public String getIdVisita() {
        return idVisita;
    }

    public void setIdVisita(String idVisita) {
        this.idVisita = idVisita;
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

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }
}
