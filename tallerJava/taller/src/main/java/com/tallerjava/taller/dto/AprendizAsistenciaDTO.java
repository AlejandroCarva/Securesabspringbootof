package com.tallerjava.taller.dto;

import java.sql.Date;

public class AprendizAsistenciaDTO {
    private Date fecha;
    private String nombreCompetencia;
    private String nombreInstructor;
    private String estadoAsistencia;
    
    // Constructor para JPQL
    public AprendizAsistenciaDTO(Date fecha, String competencia, String instructor, String estado) {
        this.fecha = fecha;
        this.nombreCompetencia = competencia;
        this.nombreInstructor = instructor;
        this.estadoAsistencia = estado;
    }
    
    // Constructor vacío
    public AprendizAsistenciaDTO() {}
    
    // Getters y Setters
    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }
    
    public String getNombreCompetencia() { return nombreCompetencia; }
    public void setNombreCompetencia(String nombreCompetencia) { this.nombreCompetencia = nombreCompetencia; }
    
    public String getNombreInstructor() { return nombreInstructor; }
    public void setNombreInstructor(String nombreInstructor) { this.nombreInstructor = nombreInstructor; }
    
    public String getEstadoAsistencia() { return estadoAsistencia; }
    public void setEstadoAsistencia(String estadoAsistencia) { this.estadoAsistencia = estadoAsistencia; }
}