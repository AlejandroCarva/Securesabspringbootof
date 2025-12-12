package com.tallerjava.taller.dto;

import java.time.LocalDate;

public class NovedadDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private String respuesta;
    private String estado;
    private String tipo;
    private LocalDate fecha;
    private String archivoAdjunto;

    // Información del instructor
    private Long instructorId;
    private String instructorNombre;
    private String instructorNombreCompleto;

    // Información de la ficha
    private Long fichaId;
    private String fichaNumero;
    private String fichaProgramaFormacion;

    // Constructores
    public NovedadDTO() {}

    public NovedadDTO(Long id, String titulo, String descripcion, String estado, LocalDate fecha) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.estado = estado;
        this.fecha = fecha;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getArchivoAdjunto() {
        return archivoAdjunto;
    }

    public void setArchivoAdjunto(String archivoAdjunto) {
        this.archivoAdjunto = archivoAdjunto;
    }

    public Long getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(Long instructorId) {
        this.instructorId = instructorId;
    }

    public String getInstructorNombre() {
        return instructorNombre;
    }

    public void setInstructorNombre(String instructorNombre) {
        this.instructorNombre = instructorNombre;
    }

    public String getInstructorNombreCompleto() {
        return instructorNombreCompleto;
    }

    public void setInstructorNombreCompleto(String instructorNombreCompleto) {
        this.instructorNombreCompleto = instructorNombreCompleto;
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

    public String getFichaProgramaFormacion() {
        return fichaProgramaFormacion;
    }

    public void setFichaProgramaFormacion(String fichaProgramaFormacion) {
        this.fichaProgramaFormacion = fichaProgramaFormacion;
    }

    // Métodos de conveniencia
    public boolean tieneRespuesta() {
        return respuesta != null && !respuesta.trim().isEmpty();
    }

    public boolean tieneArchivo() {
        return archivoAdjunto != null && !archivoAdjunto.trim().isEmpty();
    }
}