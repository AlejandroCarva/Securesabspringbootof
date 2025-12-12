package com.tallerjava.taller.service;

import com.tallerjava.taller.model.Ficha;
import java.util.List;

public interface FichaService {

    // Métodos simples usados por el coordinador
    List<Ficha> listar();
    Ficha guardar(Ficha ficha);

    // Métodos avanzados que ya tienes en el service impl
    List<Ficha> obtenerFichasPorInstructor(Integer idInstructor);

    List<Ficha> obtenerFichasPorInstructorConFiltros(
            Integer idInstructor,
            String fichaFilter,
            String jornadaFilter
    );

    Ficha obtenerPorId(Integer id);

    Ficha obtenerPorIdConAprendices(Integer id);

    boolean verificarInstructorTieneAprendiz(Integer idInstructor, Integer idAprendiz);

    List<Ficha> obtenerFichasActivasPorInstructor(Integer idInstructor);

    Long contarAprendicesPorFicha(Integer fichaId);

    boolean existeNumeroFicha(String numeroFicha);
}
