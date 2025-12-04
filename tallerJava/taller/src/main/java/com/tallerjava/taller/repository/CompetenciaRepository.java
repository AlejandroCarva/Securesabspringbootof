package com.tallerjava.taller.repository;

import com.tallerjava.taller.model.Competencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompetenciaRepository extends JpaRepository<Competencia, Long> {
    
    // ✅ MÉTODOS EXISTENTES (los mantienes)
    @Query("SELECT c FROM Competencia c WHERE c.estado = 'ACTIVA' ORDER BY c.nombreCompetencia")
    List<Competencia> findCompetenciasActivas();
    
    Competencia findByNombreCompetencia(String nombreCompetencia);
    
    @Query("SELECT c FROM Competencia c WHERE c.idPrograma = :idPrograma AND c.estado = 'ACTIVA'")
    List<Competencia> findByProgramaId(Integer idPrograma);
    
    // ✅ NUEVO MÉTODO PARA INSTRUCTOR:
    
    // Obtener competencias por programa de la ficha
    @Query(value = """
        SELECT c.* FROM competencia c
        INNER JOIN programa_competencia pc ON c.id_competencia = pc.competencia_id
        WHERE pc.programa_id = :programaId AND c.estado = 'ACTIVA'
        """, nativeQuery = true)
    List<Competencia> findByProgramaIdViaTablaIntermedia(@Param("programaId") Integer programaId);
}