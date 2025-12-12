package com.tallerjava.taller.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "programa")
public class Programa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_programa")
    private Integer id;

    @Column(name = "nombre_programa")
    private String nombrePrograma;

    @Column(name = "tipo_programa")
    private String tipoPrograma;

    @ManyToOne
    @JoinColumn(name = "id_coordinacion")
    private Coordinacion coordinacion;

    @OneToMany(mappedBy = "programa")
    private List<Ficha> fichas;

    // ---------- CONSTRUCTORES ----------
    public Programa() {
    }

    public Programa(Integer idPrograma) {
        this.id = idPrograma;
    }

    public Programa(Integer idPrograma, String nombrePrograma) {
        this.id = idPrograma;
        this.nombrePrograma = nombrePrograma;
    }

    // GETTERS Y SETTERS
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombrePrograma() {
        return nombrePrograma;
    }

    public void setNombrePrograma(String nombrePrograma) {
        this.nombrePrograma = nombrePrograma;
    }

    public String getTipoPrograma() {
        return tipoPrograma;
    }

    public void setTipoPrograma(String tipoPrograma) {
        this.tipoPrograma = tipoPrograma;
    }

    public Coordinacion getCoordinacion() {
        return coordinacion;
    }

    public void setCoordinacion(Coordinacion coordinacion) {
        this.coordinacion = coordinacion;
    }

    public List<Ficha> getFichas() {
        return fichas;
    }

    public void setFichas(List<Ficha> fichas) {
        this.fichas = fichas;
    }

    @Override
    public String toString() {
        return "Programa{" +
                "id=" + id +
                ", nombrePrograma='" + nombrePrograma + '\'' +
                '}';
    }
}