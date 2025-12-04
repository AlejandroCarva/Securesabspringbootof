package com.tallerjava.taller.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "asistencia_ambiente")
public class AsistenciaAmbiente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ✅ AGREGAR
    @Column(name = "id_asistencia_ambiente")
    private Integer id;

    // ✅ CORREGIR: Relación con USUARIO (aprendiz)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    // ✅ CORREGIR: Relación con INSTRUCTOR 
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_instructor")
    private Usuario instructor;

    // ✅ CORREGIR: Relación con COMPETENCIA (sin insertable=false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_competencia")
    private Competencia competencia;

    private LocalDate fecha; // ✅ CAMBIAR Date por LocalDate

    @Column(name = "estado_asistencia")
    private String estadoAsistencia;

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Usuario getInstructor() {
        return instructor;
    }

    public void setInstructor(Usuario instructor) {
        this.instructor = instructor;
    }

    public Competencia getCompetencia() {
        return competencia;
    }

    public void setCompetencia(Competencia competencia) {
        this.competencia = competencia;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getEstadoAsistencia() {
        return estadoAsistencia;
    }

    public void setEstadoAsistencia(String estadoAsistencia) {
        this.estadoAsistencia = estadoAsistencia;
    }
}