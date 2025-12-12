package com.tallerjava.taller.model;

import jakarta.persistence.*;

@Entity
@Table(name = "visitante")
public class Visitante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_visitante")
    private Long id;

    private String nombre;
    private String apellido;
    private String cedula;
    private String motivo;

    @Column(columnDefinition = "TEXT")  // opcional, pero útil si quieres texto largo
    private String observaciones;

    @ManyToOne
    @JoinColumn(name = "id_asistencia_sede")
    private AsistenciaSede asistenciaSede;

    // ===== Getters y setters =====
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public AsistenciaSede getAsistenciaSede() { return asistenciaSede; }
    public void setAsistenciaSede(AsistenciaSede asistenciaSede) { this.asistenciaSede = asistenciaSede; }
}
