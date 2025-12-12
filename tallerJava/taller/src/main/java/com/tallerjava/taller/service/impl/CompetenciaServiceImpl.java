package com.tallerjava.taller.service.impl;

import com.tallerjava.taller.model.Competencia;
import com.tallerjava.taller.repository.CompetenciaRepository;
import com.tallerjava.taller.service.ICompetenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompetenciaServiceImpl implements ICompetenciaService {

    @Autowired
    private CompetenciaRepository competenciaRepository;

    @Override
    public List<Competencia> obtenerTodasCompetencias() {
        return competenciaRepository.findCompetenciasActivas();
    }

    @Override
    public Competencia obtenerCompetenciaPorId(Integer id) {
        return competenciaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Competencia no encontrada"));
    }

    // ✅ NUEVO MÉTODO PARA INSTRUCTOR
    @Override
    public List<Competencia> obtenerCompetenciasPorPrograma(Integer idPrograma) {
        try {
            System.out.println("? COMPETENCIA SERVICE - Obtener competencias por programa: " + idPrograma);
            List<Competencia> competencias = competenciaRepository.findByProgramaId(idPrograma);
            System.out.println("? Competencias encontradas: " + competencias.size());
            return competencias;
        } catch (Exception e) {
            System.err.println("? ERROR al obtener competencias por programa: " + e.getMessage());
            // Intento alternativo usando la tabla intermedia
            return competenciaRepository.findByProgramaIdViaTablaIntermedia(idPrograma);
        }
    }
}