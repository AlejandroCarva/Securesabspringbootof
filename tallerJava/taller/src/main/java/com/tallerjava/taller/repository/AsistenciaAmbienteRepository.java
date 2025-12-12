package com.tallerjava.taller.repository;

import com.tallerjava.taller.model.AsistenciaAmbiente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface AsistenciaAmbienteRepository extends JpaRepository<AsistenciaAmbiente, Integer> {

	// Buscar asistencia por usuario y fecha (útil para asociar justificaciones cuando no se suministra el id)
	@Query("SELECT a FROM AsistenciaAmbiente a WHERE a.usuario.idUsuario = :usuarioId AND a.fecha = :fecha")
	Optional<AsistenciaAmbiente> findByUsuarioAndFecha(@Param("usuarioId") Integer usuarioId, @Param("fecha") LocalDate fecha);
}
