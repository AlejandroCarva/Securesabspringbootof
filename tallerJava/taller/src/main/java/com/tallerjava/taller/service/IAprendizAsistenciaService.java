package com.tallerjava.taller.service;

import com.tallerjava.taller.dto.AprendizAsistenciaDTO;
import java.util.List;

public interface IAprendizAsistenciaService {
    
    /**
     * Obtiene las asistencias de un aprendiz con filtros opcionales
     * @param usuarioId ID del usuario aprendiz
     * @param competencia Filtro por nombre de competencia (opcional)
     * @param fecha Filtro por fecha (opcional)
     * @return Lista de asistencias del aprendiz
     */
    List<AprendizAsistenciaDTO> obtenerAsistenciasPorFiltros(Integer usuarioId, String competencia, String fecha);
    
    /**
     * Obtiene todas las asistencias de un aprendiz
     * @param usuarioId ID del usuario aprendiz
     * @return Lista de todas las asistencias del aprendiz
     */
    List<AprendizAsistenciaDTO> obtenerAsistenciasPorAprendiz(Integer usuarioId);
}