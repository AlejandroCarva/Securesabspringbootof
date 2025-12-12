package com.tallerjava.taller.service.impl;

import com.tallerjava.taller.model.AsistenciaAmbiente;
import java.util.List;

public interface AsistenciaAmbienteService {
    List<AsistenciaAmbiente> listar();
    AsistenciaAmbiente guardar(AsistenciaAmbiente asistencia);
}
