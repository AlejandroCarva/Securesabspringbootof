package com.tallerjava.taller.model;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "ficha")
public class Ficha {

    @Id
    @Column(name = "id_ficha")
    private Integer idFicha;

    @Column(name = "`numeroFicha`")  // Backticks alrededor
private String numeroFicha;

    @Column(name = "fecha")
    private Date fecha;

    private String estado;

    // ✅ RELACIÓN CON JORNADA
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_jornada")
    private Jornada jornada;

    // ✅ OPCIÓN A: Si quieres mantener idPrograma como Integer Y agregar relación
    @Column(name = "id_programa", insertable = false, updatable = false)
    private Integer idPrograma; // Solo para lectura

    // ✅ OPCIÓN B: Relación con Programa (RECOMENDADA)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_programa")
    private Programa programa;

    // ✅ RELACIÓN CON APRENDICES
    @OneToMany(mappedBy = "ficha", fetch = FetchType.LAZY)
    private List<Usuario> aprendices;

    // ✅ RELACIÓN CON INSTRUCTORES (TABLA INTERMEDIA)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "ficha_instructor",
        joinColumns = @JoinColumn(name = "id_ficha"),
        inverseJoinColumns = @JoinColumn(name = "id_instructor")
    )
    private List<Usuario> instructores;

    // ---------- CONSTRUCTORES ----------
    public Ficha() {
    }

    // ---------- GETTERS & SETTERS ----------
    public Integer getIdFicha() {
        return idFicha;
    }

    public void setIdFicha(Integer idFicha) {
        this.idFicha = idFicha;
    }

    public String getNumeroFicha() {
        return numeroFicha;
    }

    public void setNumeroFicha(String numeroFicha) {
        this.numeroFicha = numeroFicha;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Jornada getJornada() {
        return jornada;
    }

    public void setJornada(Jornada jornada) {
        this.jornada = jornada;
    }

    // ✅ GETTER para idPrograma (compatible con lo que ya tienes)
    public Integer getIdPrograma() {
        return idPrograma != null ? idPrograma : 
               (programa != null ? programa.getIdPrograma() : null);
    }

    public void setIdPrograma(Integer idPrograma) {
        this.idPrograma = idPrograma;
        // Si se asigna ID, puedes crear un objeto Programa temporal
        if (idPrograma != null && programa == null) {
            this.programa = new Programa(idPrograma);
        }
    }

    // ✅ GETTER y SETTER para Programa
    public Programa getPrograma() {
        return programa;
    }

    public void setPrograma(Programa programa) {
        this.programa = programa;
        if (programa != null) {
            this.idPrograma = programa.getIdPrograma();
        }
    }

    public List<Usuario> getAprendices() {
        return aprendices;
    }

    public void setAprendices(List<Usuario> aprendices) {
        this.aprendices = aprendices;
    }

    public List<Usuario> getInstructores() {
        return instructores;
    }

    public void setInstructores(List<Usuario> instructores) {
        this.instructores = instructores;
    }

    @Override
    public String toString() {
        return "Ficha{" +
                "idFicha=" + idFicha +
                ", numeroFicha='" + numeroFicha + '\'' +
                ", programa=" + (programa != null ? programa.getNombrePrograma() : "null") +
                '}';
    }
}