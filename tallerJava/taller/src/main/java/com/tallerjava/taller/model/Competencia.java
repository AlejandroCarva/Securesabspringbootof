package com.tallerjava.taller.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

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
    
    @ManyToOne
    @JoinColumn(name = "id_programa")
    private Programa programa;

    @OneToMany(mappedBy = "competencia", fetch = FetchType.LAZY)
    private List<AsistenciaAmbiente> asistencias;

    @Column(name = "estado")
    private String estado = "ACTIVA";
}