package com.udh.apphuellitas.model;

import java.util.Date;

public class Recordatorio {
    private String idRecordatorio;
    private String idMascota;
    private String titulo;
    private Date fechaHora;
    private String tipoNotificacion;
    private boolean estadoActivo;

    public Recordatorio(String idRecordatorio, String idMascota, String titulo, Date fechaHora, String tipoNotificacion, boolean estadoActivo) {
        this.idRecordatorio = idRecordatorio;
        this.idMascota = idMascota;
        this.titulo = titulo;
        this.fechaHora = fechaHora;
        this.tipoNotificacion = tipoNotificacion;
        this.estadoActivo = estadoActivo;
    }

    public String getIdRecordatorio() {
        return idRecordatorio;
    }

    public void setIdRecordatorio(String idRecordatorio) {
        this.idRecordatorio = idRecordatorio;
    }

    public String getIdMascota() {
        return idMascota;
    }

    public void setIdMascota(String idMascota) {
        this.idMascota = idMascota;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getTipoNotificacion() {
        return tipoNotificacion;
    }

    public void setTipoNotificacion(String tipoNotificacion) {
        this.tipoNotificacion = tipoNotificacion;
    }

    public boolean isEstadoActivo() {
        return estadoActivo;
    }

    public void setEstadoActivo(boolean estadoActivo) {
        this.estadoActivo = estadoActivo;
    }
}
