package com.tallerjava.taller.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "programa")
public class Programa {
    
    @Id
    @Column(name = "id_programa")
    private Integer idPrograma;
    
    @Column(name = "nombre_programa")
    private String nombrePrograma;
    
    @Column(name = "tipo_programa")
    private String tipoPrograma;
    
    @Column(name = "id_coordinacion")
    private Integer idCoordinacion;
    
    @OneToMany(mappedBy = "programa")
    private List<Ficha> fichas;
    
    // ---------- CONSTRUCTORES ----------
    public Programa() {
    }
    
    public Programa(Integer idPrograma) {
        this.idPrograma = idPrograma;
    }
    
    public Programa(Integer idPrograma, String nombrePrograma) {
        this.idPrograma = idPrograma;
        this.nombrePrograma = nombrePrograma;
    }
    
    // ---------- GETTERS & SETTERS ----------
    public Integer getIdPrograma() {
        return idPrograma;
    }
    
    public void setIdPrograma(Integer idPrograma) {
        this.idPrograma = idPrograma;
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
    
    public Integer getIdCoordinacion() {
        return idCoordinacion;
    }
    
    public void setIdCoordinacion(Integer idCoordinacion) {
        this.idCoordinacion = idCoordinacion;
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
                "idPrograma=" + idPrograma +
                ", nombrePrograma='" + nombrePrograma + '\'' +
                '}';
    }
}