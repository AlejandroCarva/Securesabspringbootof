package com.tallerjava.taller.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "justificacion")
public class Justificacion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_justificacion")
    private Long idJustificacion;
    
    // ✅ CORREGIR: Relación correcta con AsistenciaAmbiente
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_asistencia_ambiente")
    private AsistenciaAmbiente asistenciaAmbiente;
    
    private String motivo;
    private String soporte;
    private LocalDate fecha;
    private String estado;
    private String observaciones;
}