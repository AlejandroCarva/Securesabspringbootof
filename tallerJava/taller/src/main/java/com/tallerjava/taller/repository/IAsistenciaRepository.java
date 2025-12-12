package com.tallerjava.taller.repository;

import org.springframework.stereotype.Repository; // ✅ AGREGAR ESTA LÍNEA
import com.tallerjava.taller.model.AsistenciaAmbiente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface IAsistenciaRepository extends JpaRepository<AsistenciaAmbiente, Integer> {

    // ✅ MÉTODOS EXISTENTES (los mantienes)
    @Query("SELECT a FROM AsistenciaAmbiente a WHERE a.usuario.idUsuario = :usuarioId AND a.fecha = :fecha")
    Optional<AsistenciaAmbiente> findByUsuarioIdAndFecha(@Param("usuarioId") Integer usuarioId, @Param("fecha") LocalDate fecha);
    
    @Query("SELECT a FROM AsistenciaAmbiente a WHERE a.usuario.idUsuario = :usuarioId ORDER BY a.fecha DESC")
    List<AsistenciaAmbiente> findByUsuarioIdUsuario(@Param("usuarioId") Integer usuarioId);

    // ✅ NUEVOS MÉTODOS PARA INSTRUCTOR:
    
    // 1. Obtener asistencias por fecha y lista de aprendices
    @Query("SELECT a FROM AsistenciaAmbiente a WHERE a.fecha = :fecha AND a.usuario.idUsuario IN :usuarioIds AND a.instructor.idUsuario = :instructorId")
    List<AsistenciaAmbiente> findByFechaAndUsuarioIdsAndInstructor(
        @Param("fecha") LocalDate fecha, 
        @Param("usuarioIds") List<Integer> usuarioIds, 
        @Param("instructorId") Integer instructorId);
    
    // 2. Obtener asistencias en rango de fechas
    @Query("SELECT a FROM AsistenciaAmbiente a WHERE a.fecha BETWEEN :fechaInicio AND :fechaFin AND a.usuario.idUsuario IN :usuarioIds AND a.instructor.idUsuario = :instructorId")
    List<AsistenciaAmbiente> findByFechaBetweenAndUsuarioIdsAndInstructor(
        @Param("fechaInicio") LocalDate fechaInicio,
        @Param("fechaFin") LocalDate fechaFin,
        @Param("usuarioIds") List<Integer> usuarioIds,
        @Param("instructorId") Integer instructorId);
    
    // 3. Actualizar estado de asistencia
    @Modifying
    @Transactional
    @Query("UPDATE AsistenciaAmbiente a SET a.estado = :estado WHERE a.id = :id")
    void updateEstadoAsistencia(@Param("id") Integer id, @Param("estado") String estado);

    // 4. Verificar si instructor tiene permiso sobre asistencia
    @Query("SELECT COUNT(a) > 0 FROM AsistenciaAmbiente a WHERE a.id = :id AND a.instructor.idUsuario = :instructorId")
    boolean existsByIdAndInstructorId(@Param("id") Integer id, @Param("instructorId") Integer instructorId);

    // 5. Buscar por aprendiz y fecha (alternativa)
    @Query("SELECT a FROM AsistenciaAmbiente a WHERE a.usuario.idUsuario = :usuarioId AND a.fecha = :fecha AND a.instructor.idUsuario = :instructorId")
    Optional<AsistenciaAmbiente> findByUsuarioIdAndFechaAndInstructor(
        @Param("usuarioId") Integer usuarioId, 
        @Param("fecha") LocalDate fecha,
        @Param("instructorId") Integer instructorId);
}