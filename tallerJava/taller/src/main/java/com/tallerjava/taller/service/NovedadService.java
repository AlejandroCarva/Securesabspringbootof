package com.tallerjava.taller.service;
import java.util.List;

import com.tallerjava.taller.model.Novedad;

public interface NovedadService {
    List<Novedad> listar();
    Novedad guardar(Novedad novedad);
}