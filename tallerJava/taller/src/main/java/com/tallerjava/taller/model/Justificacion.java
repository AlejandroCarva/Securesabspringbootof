package com.tallerjava.taller.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "justificacion")
public class Justificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_justificacion")
    private Integer id;

    private String motivo;
    private String soporte;
    private LocalDate fecha;
    private String estado;
    private String observaciones;

    @ManyToOne
    @JoinColumn(name = "id_asistencia_ambiente", nullable = true)
    private AsistenciaAmbiente asistenciaAmbiente;

    // GETTERS Y SETTERS
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getSoporte() {
        return soporte;
    }

    public void setSoporte(String soporte) {
        this.soporte = soporte;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
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

    public AsistenciaAmbiente getAsistenciaAmbiente() {
        return asistenciaAmbiente;
    }

    public void setAsistenciaAmbiente(AsistenciaAmbiente asistenciaAmbiente) {
        this.asistenciaAmbiente = asistenciaAmbiente;
    }
}