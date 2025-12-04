package com.tallerjava.taller.repository;

import org.springframework.stereotype.Repository; // ✅ AGREGAR ESTA LÍNEA
import com.tallerjava.taller.model.Justificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Repository
public interface JustificacionRepository extends JpaRepository<Justificacion, Long> {
    
    // ✅ MÉTODOS EXISTENTES (los mantienes)
    @Query("SELECT j FROM Justificacion j WHERE j.asistenciaAmbiente.usuario.idUsuario = :usuarioId")
    List<Justificacion> findByUsuarioId(@Param("usuarioId") Integer usuarioId);
    
    List<Justificacion> findByEstado(String estado);
    
    // ✅ NUEVOS MÉTODOS PARA INSTRUCTOR:
    
    // 1. Obtener justificaciones pendientes por instructor
    @Query(value = """
        SELECT 
            j.id_justificacion,
            j.motivo,
            j.soporte,
            j.fecha as fecha_solicitud,
            j.estado as estado_justificacion,
            aa.id_asistencia_ambiente,
            aa.fecha as fecha_inasistencia,
            aa.estado_asistencia,
            u.nombre,
            u.apellido,
            u.cedula,
            f.numeroFicha,
            c.nombre_competencia
        FROM justificacion j
        JOIN asistencia_ambiente aa ON j.id_asistencia_ambiente = aa.id_asistencia_ambiente
        JOIN usuarios u ON aa.id_usuario = u.id_usuario
        JOIN ficha f ON u.id_ficha = f.id_ficha
        JOIN competencia c ON aa.id_competencia = c.id_competencia
        WHERE j.estado = 'Pendiente'
        AND aa.id_instructor = :instructorId
        AND (:fichaId IS NULL OR u.id_ficha = :fichaId)
        AND (:competenciaId IS NULL OR aa.id_competencia = :competenciaId)
        ORDER BY j.fecha DESC
        """, nativeQuery = true)
    List<Map<String, Object>> findPendientesByInstructorId(
        @Param("instructorId") Integer instructorId,
        @Param("fichaId") Integer fichaId,
        @Param("competenciaId") Long competenciaId);
    
    // 2. Actualizar estado de justificación
    @Modifying
    @Transactional
    @Query("UPDATE Justificacion j SET j.estado = :estado, j.observaciones = :observaciones WHERE j.idJustificacion = :id")
    void updateEstadoJustificacion(@Param("id") Long id, @Param("estado") String estado, @Param("observaciones") String observaciones);
    
    // 3. Obtener justificaciones por asistencia
    @Query("SELECT j FROM Justificacion j WHERE j.asistenciaAmbiente.id = :asistenciaId")
    List<Justificacion> findByAsistenciaId(@Param("asistenciaId") Long asistenciaId);
}