package com.tallerjava.taller.service.impl;

import com.tallerjava.taller.model.AsistenciaSede;
import com.tallerjava.taller.repository.AsistenciaSedeRepository;
import com.tallerjava.taller.service.AsistenciaSedeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service   // 👈 ESTO REGISTRA EL BEAN
public class AsistenciaSedeServiceImpl implements AsistenciaSedeService {

    private final AsistenciaSedeRepository asistenciaSedeRepository;

    public AsistenciaSedeServiceImpl(AsistenciaSedeRepository asistenciaSedeRepository) {
        this.asistenciaSedeRepository = asistenciaSedeRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AsistenciaSede> listar() {
        return asistenciaSedeRepository.findAll();
    }

    @Override
    @Transactional
    public AsistenciaSede guardar(AsistenciaSede asistencia) {
        return asistenciaSedeRepository.save(asistencia);
    }
}
