package com.tallerjava.taller.service.impl;

import com.tallerjava.taller.service.NovedadService;
import com.tallerjava.taller.model.Novedad;
import com.tallerjava.taller.repository.NovedadRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NovedadServiceImpl implements NovedadService {

    private final NovedadRepository novedadRepository;

    public NovedadServiceImpl(NovedadRepository novedadRepository) {
        this.novedadRepository = novedadRepository;
    }

    @Override
    public List<Novedad> listar() {
        return novedadRepository.findAll();
    }

    @Override
    public Novedad guardar(Novedad novedad) {
        return novedadRepository.save(novedad);
    }
}
