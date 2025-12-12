package com.tallerjava.taller.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class AsistenciaDTO {
    private Long id;
    private LocalDate fecha;
    private LocalTime horaEntrada;
    private LocalTime horaSalida;
    private String estado;
    private String observaciones;
    private String tipo; // "ambiente" o "sede"

    // Información del usuario/aprendiz
    private Long usuarioId;
    private String usuarioCedula;
    private String usuarioNombre;
    private String usuarioNombreCompleto;

    // Información de la ficha
    private Long fichaId;
    private String fichaNumero;

    // Constructores
    public AsistenciaDTO() {}

    public AsistenciaDTO(Long id, LocalDate fecha, String estado, String tipo) {
        this.id = id;
        this.fecha = fecha;
        this.estado = estado;
        this.tipo = tipo;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(LocalTime horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public LocalTime getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(LocalTime horaSalida) {
        this.horaSalida = horaSalida;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getUsuarioCedula() {
        return usuarioCedula;
    }

    public void setUsuarioCedula(String usuarioCedula) {
        this.usuarioCedula = usuarioCedula;
    }

    public String getUsuarioNombre() {
        return usuarioNombre;
    }

    public void setUsuarioNombre(String usuarioNombre) {
        this.usuarioNombre = usuarioNombre;
    }

    public String getUsuarioNombreCompleto() {
        return usuarioNombreCompleto;
    }

    public void setUsuarioNombreCompleto(String usuarioNombreCompleto) {
        this.usuarioNombreCompleto = usuarioNombreCompleto;
    }

    public Long getFichaId() {
        return fichaId;
    }

    public void setFichaId(Long fichaId) {
        this.fichaId = fichaId;
    }

    public String getFichaNumero() {
        return fichaNumero;
    }

    public void setFichaNumero(String fichaNumero) {
        this.fichaNumero = fichaNumero;
    }

    // Métodos de conveniencia
    public boolean esAsistenciaAmbiente() {
        return "ambiente".equalsIgnoreCase(tipo);
    }

    public boolean esAsistenciaSede() {
        return "sede".equalsIgnoreCase(tipo);
    }

    public boolean estaPresente() {
        return "Presente".equalsIgnoreCase(estado) || "Asistio".equalsIgnoreCase(estado);
    }
}