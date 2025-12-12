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

    @NotEmpty(message = "Debe seleccionar al menos una competencia")
    private List<String> competencias;

    // === Campos adicionales ===
    private Long id;
    private String motivo;              // puedes mapearlo desde descripcionSolicitud
    private LocalDate fechaJustificacion;
    private String estado;
    private String observaciones;
    private String archivoAdjunto;

    // Información del usuario/aprendiz
    private Long usuarioId;
    private String usuarioCedula;
    private String usuarioNombre;
    private String usuarioNombreCompleto;

    // Información de la ficha
    private Long fichaId;
    private String fichaNumero;

    // 🔹 NUEVO: ID de la asistencia de ambiente asociada
    private Integer idAsistenciaAmbiente;

    public JustificacionDTO() {}

    @SuppressWarnings("unused")
    public JustificacionDTO(Long id, String motivo, LocalDate fechaJustificacion, String estado) {
        this.id = id;
        this.motivo = motivo;
        this.fechaJustificacion = fechaJustificacion;
        this.estado = estado;
    }

    @SuppressWarnings("unused")
    public boolean estaPendiente() {
        return "Pendiente".equalsIgnoreCase(estado);
    }

    @SuppressWarnings("unused")
    public boolean estaAprobada() {
        return "Aprobada".equalsIgnoreCase(estado);
    }

    @SuppressWarnings("unused")
    public boolean estaRechazada() {
        return "Rechazada".equalsIgnoreCase(estado);
    }

    @SuppressWarnings("unused")
    public boolean tieneArchivo() {
        return archivoAdjunto != null && !archivoAdjunto.trim().isEmpty();
    }
}
