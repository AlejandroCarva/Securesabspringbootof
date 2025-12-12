package com.tallerjava.taller.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "ficha")
public class Ficha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ficha")
    private Integer id;

    @Column(name = "numeroficha")
    private String numeroFicha;

    private String estado;

    private String fecha;

    @ManyToOne
    @JoinColumn(name = "id_programa")
    private Programa programa;

    @ManyToOne
    @JoinColumn(name = "id_jornada")
    private Jornada jornada;

    @OneToMany(mappedBy = "ficha")
    private List<Usuario> usuarios;

    // 🔹 NUEVO: relación con instructores (tabla ficha_instructor)
    @ManyToMany
    @JoinTable(
            name = "ficha_instructor",
            joinColumns = @JoinColumn(name = "id_ficha"),
            inverseJoinColumns = @JoinColumn(name = "id_instructor")
    )
    private List<Usuario> instructores;

    // ========== GETTERS Y SETTERS ==========

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumeroFicha() {
        return numeroFicha;
    }

    public void setNumeroFicha(String numeroFicha) {
        this.numeroFicha = numeroFicha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Programa getPrograma() {
        return programa;
    }

    public void setPrograma(Programa programa) {
        this.programa = programa;
    }

    public Jornada getJornada() {
        return jornada;
    }

    public void setJornada(Jornada jornada) {
        this.jornada = jornada;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public List<Usuario> getInstructores() {
        return instructores;
    }

    public void setInstructores(List<Usuario> instructores) {
        this.instructores = instructores;
    }
}
