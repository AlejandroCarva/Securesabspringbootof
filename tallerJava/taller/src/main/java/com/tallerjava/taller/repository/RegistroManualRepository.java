package com.tallerjava.taller.repository;

import com.tallerjava.taller.model.RegistroManual;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RegistroManualRepository extends JpaRepository<RegistroManual, Long> {

    // Obtener el último registro de un documento en el día (ordenado por fecha y hora descendente)
    @Query("SELECT r FROM RegistroManual r WHERE r.documento = :documento " +
            "AND r.fecha = :fecha ORDER BY r.fecha DESC, r.hora DESC LIMIT 1")
    java.util.Optional<RegistroManual> findLastRegistroByDocumentoAndFecha(
            @Param("documento") String documento,
            @Param("fecha") LocalDate fecha
    );

    // Obtener todos los registros de un documento en una fecha específica
    @Query("SELECT r FROM RegistroManual r WHERE r.documento = :documento " +
            "AND r.fecha = :fecha ORDER BY r.hora ASC")
    List<RegistroManual> findByDocumentoAndFecha(
            @Param("documento") String documento,
            @Param("fecha") LocalDate fecha
    );

    // Obtener todos los registros ordenados por fecha y hora descendentes
    List<RegistroManual> findAllByOrderByFechaDescHoraDesc();
}
