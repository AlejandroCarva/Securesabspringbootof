// JornadaServiceImpl.java
package com.tallerjava.taller.service.impl;

import com.tallerjava.taller.model.Jornada;
import com.tallerjava.taller.repository.IJornadaRepository;
import com.tallerjava.taller.service.IJornadaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class JornadaServiceImpl implements IJornadaService {
    
    @Autowired
    private IJornadaRepository jornadaRepository;
    
    @Override
    public List<Jornada> obtenerTodasJornadas() {
        System.out.println("? JORNADA SERVICE - Obtener todas las jornadas");
        List<Jornada> jornadas = jornadaRepository.findAll();
        System.out.println("? Jornadas encontradas: " + jornadas.size());
        return jornadas;
    }
}