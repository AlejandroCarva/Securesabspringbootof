package com.tallerjava.taller.service;

import com.tallerjava.taller.dto.JustificacionDTO;
import com.tallerjava.taller.model.Justificacion;
import com.tallerjava.taller.model.Usuario;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface IJustificacionService {
    
    // ✅ MÉTODOS EXISTENTES
    void guardarJustificacion(JustificacionDTO justificacionDTO, 
                             MultipartFile evidencia, 
                             Usuario usuario,
                             List<String> competencias);
    
    List<Justificacion> obtenerJustificacionesPorUsuario(Integer usuarioId);
    List<Justificacion> obtenerJustificacionesPorEstado(String estado);
    List<Justificacion> obtenerTodasLasJustificaciones();
    Justificacion obtenerJustificacionPorId(Long id);
    void actualizarEstadoJustificacion(Long id, String nuevoEstado);
    
    // ✅ NUEVOS MÉTODOS PARA INSTRUCTOR
    List<Map<String, Object>> obtenerJustificacionesPendientesPorInstructor(Integer instructorId, Integer fichaId, Long competenciaId);
    void procesarJustificacion(Long idJustificacion, Long idAsistenciaAmbiente, String accion, String observaciones, Integer instructorId);
}