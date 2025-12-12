package com.tallerjava.taller.service.impl;

import com.tallerjava.taller.dto.JustificacionDTO;
import com.tallerjava.taller.model.AsistenciaAmbiente;
import com.tallerjava.taller.model.Justificacion;
import com.tallerjava.taller.model.Usuario;
import com.tallerjava.taller.repository.AsistenciaAmbienteRepository;
import com.tallerjava.taller.repository.JustificacionRepository;
import com.tallerjava.taller.service.IJustificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class JustificacionServiceImpl implements IJustificacionService {

    @Autowired
    private JustificacionRepository justificacionRepository;

    @Autowired
    private AsistenciaAmbienteRepository asistenciaAmbienteRepository;

    // ==========================================================
    // 0️⃣ LISTAR (usado por el coordinador)
    // ==========================================================
    @Override
    public List<Justificacion> listar() {
        return justificacionRepository.findAll();
    }

    // ==========================================================
    // 1️⃣ GUARDAR JUSTIFICACIÓN (CREACIÓN)
    // ==========================================================
    @Override
    public void guardarJustificacion(JustificacionDTO dto,
                                     MultipartFile evidencia,
                                     Usuario usuario,
                                     List<String> competencias) {

        try {
            Justificacion justificacion = new Justificacion();

            // Mapear datos del DTO a la entidad
            // Usamos descripcionSolicitud como motivo
            justificacion.setMotivo(dto.getDescripcionSolicitud());
            justificacion.setObservaciones(dto.getOtrasJustificaciones());

            if (evidencia != null && !evidencia.isEmpty()) {
                justificacion.setSoporte(evidencia.getOriginalFilename());
            } else {
                justificacion.setSoporte(null);
            }

            justificacion.setFecha(LocalDate.now());
            justificacion.setEstado("Pendiente");

            // Asociar asistencia si viene el id en el DTO
            // Asociar asistencia si viene el id en el DTO
            if (dto.getIdAsistenciaAmbiente() != null) {
                Optional<AsistenciaAmbiente> asistenciaOpt = asistenciaAmbienteRepository.findById(dto.getIdAsistenciaAmbiente());
                asistenciaOpt.ifPresent(justificacion::setAsistenciaAmbiente);
            } else {
                // Intentar asociar por usuario + fechaInasistencia si está disponible
                try {
                    if (dto.getFechaInasistencia() != null) {
                        Optional<AsistenciaAmbiente> asistenciaPorFecha = asistenciaAmbienteRepository.findByUsuarioAndFecha(usuario.getIdUsuario(), dto.getFechaInasistencia());
                        if (asistenciaPorFecha.isPresent()) {
                            justificacion.setAsistenciaAmbiente(asistenciaPorFecha.get());
                        } else {
                            justificacion.setAsistenciaAmbiente(null);
                        }
                    } else {
                        justificacion.setAsistenciaAmbiente(null);
                    }
                } catch (Exception e) {
                    // En caso de cualquier error al buscar, no bloquear el guardado: dejar null
                    justificacion.setAsistenciaAmbiente(null);
                }
            }

            // (Tu entidad Justificacion ya no tiene usuario ni competencias, así que no se setean)

            justificacionRepository.save(justificacion);

        } catch (Exception e) {
            throw new RuntimeException("Error guardando la justificación: " + e.getMessage());
        }
    }

    // ==========================================================
    // 2️⃣ LISTAR POR USUARIO
    // ==========================================================
    @Override
    public List<Justificacion> obtenerJustificacionesPorUsuario(Integer usuarioId) {
        return justificacionRepository.findByUsuarioId(usuarioId);
    }

    // ==========================================================
    // 3️⃣ LISTAR POR ESTADO
    // ==========================================================
    @Override
    public List<Justificacion> obtenerJustificacionesPorEstado(String estado) {
        return justificacionRepository.findByEstado(estado);
    }

    // ==========================================================
    // 4️⃣ LISTAR TODAS
    // ==========================================================
    @Override
    public List<Justificacion> obtenerTodasLasJustificaciones() {
        return justificacionRepository.findAll();
    }

    // ==========================================================
    // 5️⃣ OBTENER POR ID
    // ==========================================================
    @Override
    public Justificacion obtenerJustificacionPorId(Integer id) {
        return justificacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Justificación no encontrada"));
    }

    // ==========================================================
    // 6️⃣ ACTUALIZAR ESTADO
    // ==========================================================
    @Override
    public void actualizarEstadoJustificacion(Integer id, String nuevoEstado) {
        Justificacion justificacion = obtenerJustificacionPorId(id);
        justificacion.setEstado(nuevoEstado);
        justificacionRepository.save(justificacion);
    }

    // ==========================================================
    // 7️⃣ JUSTIFICACIONES PENDIENTES PARA UN INSTRUCTOR
    // ==========================================================
    @Override
    public List<Map<String, Object>> obtenerJustificacionesPendientesPorInstructor(
            Integer instructorId, Integer fichaId, Long competenciaId) {

        return justificacionRepository.findPendientesByInstructorId(
                instructorId, fichaId, competenciaId
        );
    }

    // ==========================================================
    // 8️⃣ PROCESAR JUSTIFICACIÓN (APROBAR / RECHAZAR)
    // ==========================================================
    @Override
    public void procesarJustificacion(Integer idJustificacion,
                                      Integer idAsistenciaAmbiente,
                                      String accion,
                                      String observaciones,
                                      Integer instructorId) {

        Justificacion justificacion = obtenerJustificacionPorId(idJustificacion);

        AsistenciaAmbiente asistencia = asistenciaAmbienteRepository.findById(idAsistenciaAmbiente)
                .orElseThrow(() -> new RuntimeException("Asistencia no encontrada para ID: " + idAsistenciaAmbiente));

        // Tu entidad AsistenciaAmbiente NO nos la has pasado,
        // así que por ahora solo actualizamos la justificación.
        if ("Aprobar".equalsIgnoreCase(accion)) {
            justificacion.setEstado("Aprobado");
        } else if ("Rechazar".equalsIgnoreCase(accion)) {
            justificacion.setEstado("Rechazado");
        } else {
            throw new RuntimeException("Acción inválida: " + accion);
        }

        justificacion.setObservaciones(observaciones);

        asistenciaAmbienteRepository.save(asistencia);
        justificacionRepository.save(justificacion);
    }
}
