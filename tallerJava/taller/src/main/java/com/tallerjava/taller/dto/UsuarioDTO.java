package com.tallerjava.taller.dto;

import java.time.LocalDateTime;
import java.util.List;

public class UsuarioDTO {
    private Long id;
    private String cedula;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private String estado;
    private LocalDateTime fechaCreacion;
    private List<String> roles;
    private String fichaNumero;

    // Constructores
    public UsuarioDTO() {}

    public UsuarioDTO(Long id, String cedula, String nombre, String apellido, String email) {
        this.id = id;
        this.cedula = cedula;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getFichaNumero() {
        return fichaNumero;
    }

    public void setFichaNumero(String fichaNumero) {
        this.fichaNumero = fichaNumero;
    }

    public String getNombreCompleto() {
        return (nombre != null ? nombre : "") +
                (apellido != null ? " " + apellido : "");
    }
}