package com.tallerjava.taller.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "competencia")
public class Competencia {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_competencia")
    private Integer idCompetencia;
    
    @Column(name = "nombre_competencia")
    private String nombreCompetencia;
    
    private String descripcion;
    
    @Column(name = "id_programa")
    private Integer idPrograma;
    
    @Column(name = "estado")
    private String estado = "ACTIVA";
}