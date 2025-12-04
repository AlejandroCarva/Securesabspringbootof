// IInstructorAsistenciaService.java
package com.tallerjava.taller.service;

import com.tallerjava.taller.dto.GuardarAsistenciasDTO;
import java.time.LocalDate;
import java.util.Map;

public interface IInstructorAsistenciaService {
    Map<Integer, String> obtenerAsistenciasPorFechaYFicha(LocalDate fecha, Integer fichaId, Integer instructorId);
    void guardarAsistencias(GuardarAsistenciasDTO dto);
    Map<String, Object> obtenerReporteAsistencias(Integer fichaId, Integer instructorId, LocalDate fechaInicio, LocalDate fechaFin);
}