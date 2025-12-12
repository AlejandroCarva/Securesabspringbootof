package com.tallerjava.taller.service.impl;

import com.tallerjava.taller.dto.AprendizAsistenciaDTO;
import com.tallerjava.taller.model.AsistenciaAmbiente;
import com.tallerjava.taller.repository.IAsistenciaRepository;
import com.tallerjava.taller.service.IAprendizAsistenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service
public class AprendizAsistenciaServiceImpl implements IAprendizAsistenciaService {

    @Autowired
    private IAsistenciaRepository asistenciaRepository;

    @Override
    public List<AprendizAsistenciaDTO> obtenerAsistenciasPorFiltros(Integer usuarioId, String competencia, String fecha) {
        try {
            System.out.println("? SERVICE - Obteniendo asistencias para usuario: " + usuarioId);
            System.out.println("? Filtros - competencia: " + competencia + ", fecha: " + fecha);
            
            // ✅ OBTENER DATOS REALES de la base de datos
            List<AsistenciaAmbiente> asistenciasEntities = asistenciaRepository.findByUsuarioIdUsuario(usuarioId);

            System.out.println("? Asistencias encontradas en BD: " + asistenciasEntities.size());
            
            // Convertir entidades a DTOs
            List<AprendizAsistenciaDTO> asistenciasDTO = new ArrayList<>();
            
            for (AsistenciaAmbiente asistencia : asistenciasEntities) {
                // Crear DTO con los datos de la entidad
                AprendizAsistenciaDTO dto = new AprendizAsistenciaDTO(
                    Date.valueOf(asistencia.getFecha()), // Convertir LocalDate a java.sql.Date
                    asistencia.getCompetencia() != null ? asistencia.getCompetencia().getNombreCompetencia() : "N/A",
                    asistencia.getInstructor() != null ? 
                        asistencia.getInstructor().getNombre() + " " + asistencia.getInstructor().getApellido() : "N/A",
                    asistencia.getEstado() != null ? asistencia.getEstado() : "N/A"
                );
                asistenciasDTO.add(dto);
            }
            
            System.out.println("? DTOs convertidos: " + asistenciasDTO.size());
            
            // Aplicar filtros si se especificaron
            if ((competencia != null && !competencia.trim().isEmpty()) || 
                (fecha != null && !fecha.trim().isEmpty())) {
                
                List<AprendizAsistenciaDTO> asistenciasFiltradas = new ArrayList<>();
                
                for (AprendizAsistenciaDTO dto : asistenciasDTO) {
                    boolean coincideCompetencia = true;
                    boolean coincideFecha = true;
                    
                    // Filtrar por competencia
                    if (competencia != null && !competencia.trim().isEmpty()) {
                        coincideCompetencia = dto.getNombreCompetencia().toLowerCase()
                            .contains(competencia.toLowerCase());
                    }
                    
                    // Filtrar por fecha
                    if (fecha != null && !fecha.trim().isEmpty()) {
                        coincideFecha = dto.getFecha().toString().equals(fecha);
                    }
                    
                    if (coincideCompetencia && coincideFecha) {
                        asistenciasFiltradas.add(dto);
                    }
                }
                
                System.out.println("? Asistencias después de filtrar: " + asistenciasFiltradas.size());
                return asistenciasFiltradas;
            }
            
            return asistenciasDTO;
            
        } catch (Exception e) {
            System.err.println("? ERROR en servicio obtenerAsistenciasPorFiltros: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<AprendizAsistenciaDTO> obtenerAsistenciasPorAprendiz(Integer usuarioId) {
        try {
            System.out.println("? SERVICE - Obteniendo asistencias para aprendiz ID: " + usuarioId);
            
            // ✅ OBTENER DATOS REALES
            List<AsistenciaAmbiente> asistenciasEntities = asistenciaRepository.findByUsuarioIdUsuario(usuarioId);

            System.out.println("? Asistencias encontradas: " + asistenciasEntities.size());
            
            // Convertir entidades a DTOs
            List<AprendizAsistenciaDTO> asistenciasDTO = new ArrayList<>();
            
            for (AsistenciaAmbiente asistencia : asistenciasEntities) {
                AprendizAsistenciaDTO dto = new AprendizAsistenciaDTO(
                    Date.valueOf(asistencia.getFecha()),
                    asistencia.getCompetencia() != null ? asistencia.getCompetencia().getNombreCompetencia() : "N/A",
                    asistencia.getInstructor() != null ? 
                        asistencia.getInstructor().getNombre() + " " + asistencia.getInstructor().getApellido() : "N/A",
                    asistencia.getEstado() != null ? asistencia.getEstado() : "N/A"
                );
                asistenciasDTO.add(dto);
            }
            
            return asistenciasDTO;
            
        } catch (Exception e) {
            System.err.println("? ERROR en servicio obtenerAsistenciasPorAprendiz: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}