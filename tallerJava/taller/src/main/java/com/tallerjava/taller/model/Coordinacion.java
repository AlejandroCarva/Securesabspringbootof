package com.tallerjava.taller.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "coordinacion")
public class Coordinacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_coordinacion")
    private Integer id;

    private String nombreCoordinacion;
    private String descripcion;
}
