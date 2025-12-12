package com.tallerjava.taller.service.impl;

import com.tallerjava.taller.model.AsistenciaAmbiente;
import com.tallerjava.taller.repository.AsistenciaAmbienteRepository;
import com.tallerjava.taller.service.AsistenciaAmbienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AsistenciaAmbienteServiceImpl implements AsistenciaAmbienteService {

    @Autowired
    private AsistenciaAmbienteRepository asistenciaAmbienteRepository;

    @Override
    public List<AsistenciaAmbiente> listar() {
        return asistenciaAmbienteRepository.findAll();
    }
}
