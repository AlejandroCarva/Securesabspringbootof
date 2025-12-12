package com.tallerjava.taller.repository;

import com.tallerjava.taller.model.Visitante;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface VisitanteRepository extends JpaRepository<Visitante, Long> {

    Optional<Visitante> findByCedula(String cedula);

    @Query("""
        SELECT v FROM Visitante v
        WHERE REPLACE(REPLACE(v.cedula, ' ', ''), '-', '') =
              REPLACE(REPLACE(:cedula, ' ', ''), '-', '')
        """)
    Optional<Visitante> findByCedulaFlexible(@Param("cedula") String cedula);

    @Query(
            value = """
            SELECT v FROM Visitante v
            LEFT JOIN v.asistenciaSede a
            WHERE (:nombre IS NULL OR :nombre = '' 
                   OR LOWER(v.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')))
              AND (:cedula IS NULL OR :cedula = '' OR v.cedula = :cedula)
              AND (:fechaDesde IS NULL OR a.fecha >= :fechaDesde)
              AND (:fechaHasta IS NULL OR a.fecha <= :fechaHasta)
            """,
            countQuery = """
            SELECT COUNT(v) FROM Visitante v
            LEFT JOIN v.asistenciaSede a
            WHERE (:nombre IS NULL OR :nombre = '' 
                   OR LOWER(v.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')))
              AND (:cedula IS NULL OR :cedula = '' OR v.cedula = :cedula)
              AND (:fechaDesde IS NULL OR a.fecha >= :fechaDesde)
              AND (:fechaHasta IS NULL OR a.fecha <= :fechaHasta)
            """
    )
    Page<Visitante> buscarPorFiltros(
            @Param("nombre") String nombre,
            @Param("cedula") String cedula,
            @Param("fechaDesde") LocalDate fechaDesde,
            @Param("fechaHasta") LocalDate fechaHasta,
            Pageable pageable
    );
}
