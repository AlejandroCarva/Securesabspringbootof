package com.tallerjava.taller.service;

import com.tallerjava.taller.model.Competencia;
import java.util.List;

public interface ICompetenciaService {
    
    // ✅ MÉTODOS EXISTENTES
    List<Competencia> obtenerTodasCompetencias();
    Competencia obtenerCompetenciaPorId(Integer id);
    
    // ✅ NUEVO MÉTODO PARA INSTRUCTOR
    List<Competencia> obtenerCompetenciasPorPrograma(Integer idPrograma);
}   