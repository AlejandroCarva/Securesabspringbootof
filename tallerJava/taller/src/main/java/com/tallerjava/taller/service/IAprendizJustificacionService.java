package com.tallerjava.taller.service;

import com.tallerjava.taller.dto.AprendizJustificacionDTO;
import java.util.List;

public interface IAprendizJustificacionService {

    List<AprendizJustificacionDTO> listar(Integer idUsuario);

    AprendizJustificacionDTO obtenerPorId(Integer id);

}
