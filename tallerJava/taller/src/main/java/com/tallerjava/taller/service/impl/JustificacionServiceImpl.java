package com.tallerjava.taller.service.impl;

import com.tallerjava.taller.dto.JustificacionDTO;
import com.tallerjava.taller.model.Justificacion;
import com.tallerjava.taller.model.Usuario;
import com.tallerjava.taller.repository.JustificacionRepository;
import com.tallerjava.taller.service.IJustificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
public class JustificacionServiceImpl implements IJustificacionService {

    @Autowired
    private JustificacionRepository justificacionRepository;

    // ✅ MÉTODOS EXISTENTES (los mantienes como están)
    @Override
    public void guardarJustificacion(JustificacionDTO justificacionDTO, 
                                    MultipartFile evidencia, 
                                    Usuario usuario,
                                    List<String> competencias) {
        // Tu implementación existente
    }

    @Override
    public List<Justificacion> obtenerJustificacionesPorUsuario(Integer usuarioId) {
        // Tu implementación existente
        return justificacionRepository.findByUsuarioId(usuarioId);
    }

    @Override
    public List<Justificacion> obtenerJustificacionesPorEstado(String estado) {
        return justificacionRepository.findByEstado(estado);
    }

    @Override
    public List<Justificacion> obtenerTodasLasJustificaciones() {
        return justificacionRepository.findAll();
    }

    @Override
    public Justificacion obtenerJustificacionPorId(Long id) {
        return justificacionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Justificación no encontrada"));
    }

    @Override
    public void actualizarEstadoJustificacion(Long id, String nuevoEstado) {
        Justificacion justificacion = obtenerJustificacionPorId(id);
        justificacion.setEstado(nuevoEstado);
        justificacionRepository.save(justificacion);
    }

    // ✅ NUEVOS MÉTODOS PARA INSTRUCTOR
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> obtenerJustificacionesPendientesPorInstructor(Integer instructorId, Integer fichaId, Long competenciaId) {
        try {
            System.out.println("? JUSTIFICACION SERVICE - Obtener pendientes por instructor");
            System.out.println("? Instructor: " + instructorId + ", Ficha: " + fichaId + ", Competencia: " + competenciaId);
            
            List<Map<String, Object>> novedades = justificacionRepository
                .findPendientesByInstructorId(instructorId, fichaId, competenciaId);
            
            System.out.println("? Novedades encontradas: " + novedades.size());
            return novedades;
            
        } catch (Exception e) {
            System.err.println("? ERROR al obtener novedades: " + e.getMessage());
            throw new RuntimeException("Error al obtener justificaciones pendientes: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void procesarJustificacion(Long idJustificacion, Long idAsistenciaAmbiente, String accion, String observaciones, Integer instructorId) {
        try {
            System.out.println("? JUSTIFICACION SERVICE - Procesar justificación");
            System.out.println("? Justificación ID: " + idJustificacion + ", Asistencia ID: " + idAsistenciaAmbiente);
            
            // Determinar nuevos estados
            String nuevoEstadoAsistencia = "Aceptar".equals(accion) ? "Justificado" : "Inasistio";
            String nuevoEstadoJustificacion = "Aceptar".equals(accion) ? "Aprobada" : "Rechazada";
            
            // Actualizar estado de la justificación
            justificacionRepository.updateEstadoJustificacion(
                idJustificacion, nuevoEstadoJustificacion, observaciones);
            
            System.out.println("? Justificación procesada: " + nuevoEstadoJustificacion);
            
        } catch (Exception e) {
            System.err.println("? ERROR al procesar justificación: " + e.getMessage());
            throw new RuntimeException("Error al procesar justificación: " + e.getMessage(), e);
        }
    }
}