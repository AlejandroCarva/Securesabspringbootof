package com.tallerjava.taller.service;

import com.tallerjava.taller.model.AsistenciaSede;
import java.util.List;

public interface AsistenciaSedeService {
    List<AsistenciaSede> listar();
    AsistenciaSede guardar(AsistenciaSede asistencia);
}
