package com.tallerjava.taller.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class JustificacionDTO {
    
    @NotNull(message = "La fecha de solicitud es obligatoria")
    private LocalDate fechaSolicitud;
    
    @NotNull(message = "La fecha de inasistencia es obligatoria")
    private LocalDate fechaInasistencia;
    
    @NotEmpty(message = "La descripción/motivo es obligatoria")
    private String descripcionSolicitud;
    
    private String otrasJustificaciones;
    
    // ✅ CORREGIDO: Debe ser List<String> para que funcione con el servicio
    @NotEmpty(message = "Debe seleccionar al menos una competencia")
    private List<String> competencias;
}