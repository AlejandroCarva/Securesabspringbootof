package com.tallerjava.taller.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Data
public class GuardarAsistenciasDTO {
    private Integer fichaId;
    private LocalDate fechaAsistencia;
    private Integer competenciaId;
    
    // ✅ INICIALIZA EL MAP para evitar NullPointerException
    private Map<Integer, String> asistencias = new HashMap<>(); 
    
    private Integer instructorId; // Se setea en el controller
    
    // ✅ Constructor que inicializa el map
    public GuardarAsistenciasDTO() {
        this.asistencias = new HashMap<>();
    }
    
    // ✅ Constructor con parámetros
    public GuardarAsistenciasDTO(Integer fichaId, LocalDate fechaAsistencia, 
                               Integer competenciaId, Map<Integer, String> asistencias) {
        this.fichaId = fichaId;
        this.fechaAsistencia = fechaAsistencia;
        this.competenciaId = competenciaId;
        this.asistencias = asistencias != null ? asistencias : new HashMap<>();
    }
    
    // ✅ Getter seguro
    public Map<Integer, String> getAsistencias() {
        if (this.asistencias == null) {
            this.asistencias = new HashMap<>();
        }
        return this.asistencias;
    }
    
    // ✅ Setter seguro
    public void setAsistencias(Map<Integer, String> asistencias) {
        this.asistencias = asistencias != null ? asistencias : new HashMap<>();
    }
}