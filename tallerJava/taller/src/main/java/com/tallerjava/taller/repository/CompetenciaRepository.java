package com.tallerjava.taller.repository;

import com.tallerjava.taller.model.Competencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompetenciaRepository extends JpaRepository<Competencia, Integer> {

    // Obtener competencias activas ordenadas por nombre
    @Query("SELECT c FROM Competencia c WHERE c.estado = 'ACTIVA' ORDER BY c.nombreCompetencia")
    List<Competencia> findCompetenciasActivas();

    // Buscar por nombre
    Competencia findByNombreCompetencia(String nombreCompetencia);

    // Buscar por id de programa (usando la relación programa.id)
    @Query("SELECT c FROM Competencia c WHERE c.programa.id = :idPrograma AND c.estado = 'ACTIVA'")
    List<Competencia> findByProgramaId(@Param("idPrograma") Integer idPrograma);

    // Cargar competencias por programa incluyendo las asistencias (fetch join)
    @Query("""
           SELECT DISTINCT c
           FROM Competencia c
           LEFT JOIN FETCH c.asistencias a
           WHERE c.programa.id = :programaId
           AND c.estado = 'ACTIVA'
           """)
    List<Competencia> findByProgramaIdWithAsistencias(@Param("programaId") Integer programaId);

    // Contar competencias activas por programa
    @Query("SELECT COUNT(c) FROM Competencia c WHERE c.programa.id = :programaId AND c.estado = 'ACTIVA'")
    Long countByProgramaId(@Param("programaId") Integer programaId);

    // Método nativo opcional (este sí usa nombres de columnas de BD)
    @Query(value = """
        SELECT c.* FROM competencia c
        INNER JOIN programa_competencia pc ON c.id_competencia = pc.competencia_id
        WHERE pc.programa_id = :programaId AND c.estado = 'ACTIVA'
        """, nativeQuery = true)
    List<Competencia> findByProgramaIdViaTablaIntermedia(@Param("programaId") Integer programaId);
}
