package com.tallerjava.taller.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class RegistroManualDTO {

    private String documento;
    private String nombre;
    private String apellido;
    private String motivo;
    private LocalDate fecha;
    private LocalTime horaEntrada;
    private LocalTime horaSalida;
    private String tipoMovimiento;

    public RegistroManualDTO(String documento, String nombre, String apellido,
                             String motivo, LocalDate fecha, LocalTime horaEntrada,
                             LocalTime horaSalida) {
        this.documento = documento;
        this.nombre = nombre;
        this.apellido = apellido;
        this.motivo = motivo;
        this.fecha = fecha;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
    }

    // Getters
    public String getDocumento() {
        return documento;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getMotivo() {
        return motivo;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public LocalTime getHoraEntrada() {
        return horaEntrada;
    }

    public LocalTime getHoraSalida() {
        return horaSalida;
    }

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    // Setters
    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public void setHoraEntrada(LocalTime horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public void setHoraSalida(LocalTime horaSalida) {
        this.horaSalida = horaSalida;
    }

    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }
}

