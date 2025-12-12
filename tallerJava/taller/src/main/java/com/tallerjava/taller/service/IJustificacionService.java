package com.tallerjava.taller.service;

import com.tallerjava.taller.dto.JustificacionDTO;
import com.tallerjava.taller.model.Justificacion;
import com.tallerjava.taller.model.Usuario;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface IJustificacionService {

    // Usado por JustificacionController (coordinador)
    List<Justificacion> listar();

    // Crear justificación
    void guardarJustificacion(JustificacionDTO justificacionDTO,
                              MultipartFile evidencia,
                              Usuario usuario,
                              List<String> competencias);

    // Consultas varias
    List<Justificacion> obtenerJustificacionesPorUsuario(Integer usuarioId);
    List<Justificacion> obtenerJustificacionesPorEstado(String estado);
    List<Justificacion> obtenerTodasLasJustificaciones();
    Justificacion obtenerJustificacionPorId(Integer id);
    void actualizarEstadoJustificacion(Integer id, String nuevoEstado);

    // Para vistas del instructor
    List<Map<String, Object>> obtenerJustificacionesPendientesPorInstructor(
            Integer instructorId, Integer fichaId, Long competenciaId);

    void procesarJustificacion(Integer idJustificacion,
                               Integer idAsistenciaAmbiente,
                               String accion,
                               String observaciones,
                               Integer instructorId);
}
