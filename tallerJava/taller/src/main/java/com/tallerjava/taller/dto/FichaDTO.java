package com.tallerjava.taller.dto;

public class FichaDTO {
    private Long id;
    private String numeroFicha;
    private String programaFormacion;
    private String jornada;
    private String coordinacion;
    private String estado;
    private Integer cantidadAprendices;
    private String instructorPrincipal;

    // Constructores
    public FichaDTO() {}

    public FichaDTO(Long id, String numeroFicha, String programaFormacion, String jornada) {
        this.id = id;
        this.numeroFicha = numeroFicha;
        this.programaFormacion = programaFormacion;
        this.jornada = jornada;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroFicha() {
        return numeroFicha;
    }

    public void setNumeroFicha(String numeroFicha) {
        this.numeroFicha = numeroFicha;
    }

    public String getProgramaFormacion() {
        return programaFormacion;
    }

    public void setProgramaFormacion(String programaFormacion) {
        this.programaFormacion = programaFormacion;
    }

    public String getJornada() {
        return jornada;
    }

    public void setJornada(String jornada) {
        this.jornada = jornada;
    }

    public String getCoordinacion() {
        return coordinacion;
    }

    public void setCoordinacion(String coordinacion) {
        this.coordinacion = coordinacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getCantidadAprendices() {
        return cantidadAprendices;
    }

    public void setCantidadAprendices(Integer cantidadAprendices) {
        this.cantidadAprendices = cantidadAprendices;
    }

    public String getInstructorPrincipal() {
        return instructorPrincipal;
    }

    public void setInstructorPrincipal(String instructorPrincipal) {
        this.instructorPrincipal = instructorPrincipal;
    }
}