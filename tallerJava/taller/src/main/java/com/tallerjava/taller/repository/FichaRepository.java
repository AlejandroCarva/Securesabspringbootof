package com.tallerjava.taller.repository;

import com.tallerjava.taller.model.Ficha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FichaRepository extends JpaRepository<Ficha, Integer> {

    // 1. PRINCIPAL: Fichas del instructor con relaciones cargadas
    @Query("""
        SELECT DISTINCT f FROM Ficha f 
        LEFT JOIN FETCH f.jornada j
        LEFT JOIN FETCH f.programa p
        LEFT JOIN FETCH f.instructores i
        WHERE i.idUsuario = :instructorId
        AND f.estado = 'Activa'
        ORDER BY f.numeroFicha
        """)
    List<Ficha> findByInstructorIdWithRelations(@Param("instructorId") Integer instructorId);

    // 2. Método anterior para compatibilidad (nativa)
    @Query(value = """
        SELECT f.* FROM ficha f
        INNER JOIN ficha_instructor fi ON f.id_ficha = fi.id_ficha
        WHERE fi.id_instructor = :instructorId
        AND f.estado = 'Activa'
        ORDER BY f.numeroFicha
        """, nativeQuery = true)
    List<Ficha> findByInstructorId(@Param("instructorId") Integer instructorId);

    // 3. Fichas activas por instructor (nativa)
    @Query(value = """
        SELECT f.* FROM ficha f
        INNER JOIN ficha_instructor fi ON f.id_ficha = fi.id_ficha
        WHERE fi.id_instructor = :instructorId AND f.estado = 'Activa'
        ORDER BY f.numeroFicha
        """, nativeQuery = true)
    List<Ficha> findActiveByInstructorId(@Param("instructorId") Integer instructorId);

    // 4. Verificar si instructor tiene acceso a ficha (nativa)
    @Query(value = """
        SELECT COUNT(*) > 0 FROM ficha_instructor 
        WHERE id_ficha = :fichaId AND id_instructor = :instructorId
        """, nativeQuery = true)
    boolean existsByFichaIdAndInstructorId(
            @Param("fichaId") Integer fichaId,
            @Param("instructorId") Integer instructorId);

    // 5. Buscar por número de ficha
    List<Ficha> findByNumeroFichaContaining(String numeroFicha);

    // 6. Buscar fichas por jornada (usar j.id, NO idJornada)
    @Query("SELECT f FROM Ficha f WHERE f.jornada.id = :jornadaId")
    List<Ficha> findByJornadaId(@Param("jornadaId") Integer jornadaId);

    // 7. Método con filtros
    @Query("""
        SELECT DISTINCT f FROM Ficha f 
        LEFT JOIN FETCH f.jornada j
        LEFT JOIN FETCH f.programa p
        LEFT JOIN FETCH f.instructores i
        WHERE i.idUsuario = :instructorId
        AND (:numeroFicha IS NULL OR f.numeroFicha LIKE %:numeroFicha%)
        AND (:jornadaNombre IS NULL OR j.nombreJornada = :jornadaNombre)
        AND f.estado = 'Activa'
        ORDER BY f.numeroFicha
        """)
    List<Ficha> findByInstructorIdWithFilters(
            @Param("instructorId") Integer instructorId,
            @Param("numeroFicha") String numeroFicha,
            @Param("jornadaNombre") String jornadaNombre);

    // 8. Ficha con todas las relaciones
    @Query("""
        SELECT DISTINCT f FROM Ficha f 
        LEFT JOIN FETCH f.jornada j
        LEFT JOIN FETCH f.programa p
        LEFT JOIN FETCH f.instructores i
        WHERE f.id = :fichaId
        """)
    Optional<Ficha> findByIdWithAllRelations(@Param("fichaId") Integer fichaId);

    // 9. Ficha con programa
    @Query("""
        SELECT f FROM Ficha f 
        LEFT JOIN FETCH f.programa p
        WHERE f.id = :fichaId
        """)
    Optional<Ficha> findByIdWithPrograma(@Param("fichaId") Integer fichaId);

    // 10. Fichas por instructor y programa (usar p.id)
    @Query("""
        SELECT f FROM Ficha f 
        LEFT JOIN FETCH f.programa p
        LEFT JOIN FETCH f.instructores i
        WHERE i.idUsuario = :instructorId
        AND p.id = :programaId
        AND f.estado = 'Activa'
        """)
    List<Ficha> findByInstructorAndPrograma(
            @Param("instructorId") Integer instructorId,
            @Param("programaId") Integer programaId);

    // 11. Contar aprendices por ficha (nativa)
    @Query(value = """
        SELECT COUNT(*) FROM usuarios u
        WHERE u.id_ficha = :fichaId 
        AND u.estado = 'activo'
        """, nativeQuery = true)
    Long countAprendicesByFichaId(@Param("fichaId") Integer fichaId);

    // 12. Verificar si número de ficha existe
    @Query("SELECT COUNT(f) > 0 FROM Ficha f WHERE f.numeroFicha = :numeroFicha")
    boolean existsByNumeroFicha(@Param("numeroFicha") String numeroFicha);

    // 13. Buscar fichas por estado
    List<Ficha> findByEstado(String estado);

    // 14. Todas las fichas activas con programa y jornada
    @Query("""
        SELECT f FROM Ficha f 
        LEFT JOIN FETCH f.programa p
        LEFT JOIN FETCH f.jornada j
        WHERE f.estado = 'Activa'
        ORDER BY f.numeroFicha
        """)
    List<Ficha> findAllActiveWithProgramaAndJornada();
}
