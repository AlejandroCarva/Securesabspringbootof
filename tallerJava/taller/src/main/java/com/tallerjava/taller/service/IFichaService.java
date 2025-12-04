package com.tallerjava.taller.service;

import com.tallerjava.taller.model.Ficha;
import java.util.List;

public interface IFichaService {
    
    // ✅ Obtener fichas de un instructor (sin filtros)
    List<Ficha> obtenerFichasPorInstructor(Integer idInstructor);
    
    // ✅ Obtener fichas de un instructor con filtros (ficha y jornada)
    List<Ficha> obtenerFichasPorInstructorConFiltros(Integer idInstructor, String fichaFilter, String jornadaFilter);
    
    // ✅ Obtener ficha por ID
    Ficha obtenerPorId(Integer id);
    
    // ✅ Obtener ficha por ID con aprendices cargados
    Ficha obtenerPorIdConAprendices(Integer id);
    
    // ✅ Verificar si un instructor tiene acceso a un aprendiz
    boolean verificarInstructorTieneAprendiz(Integer idInstructor, Integer idAprendiz);
    
    // ✅ Obtener fichas activas de un instructor
    List<Ficha> obtenerFichasActivasPorInstructor(Integer idInstructor);
}