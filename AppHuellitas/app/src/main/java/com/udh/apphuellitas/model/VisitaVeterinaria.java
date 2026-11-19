package com.udh.apphuellitas.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "visitas_veterinarias")
public class VisitaVeterinaria {

    @PrimaryKey
    @NonNull
    private String idVisita = "";

    private String idMascota;
    private String fecha;
    private String lugar;
    private String motivo;
    private String diagnostico;

    public VisitaVeterinaria() {
    }

    @NonNull
    public String getIdVisita() {
        return idVisita;
    }

    public void setIdVisita(@NonNull String idVisita) {
        this.idVisita = idVisita;
    }

    public String getIdMascota() {
        return idMascota;
    }

    public void setIdMascota(String idMascota) {
        this.idMascota = idMascota;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
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
