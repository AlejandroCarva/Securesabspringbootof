package com.tallerjava.taller.service;


import com.tallerjava.taller.model.Justificacion;
import java.util.List;

public interface JustificacionService {
    List<Justificacion> listar();
    Justificacion guardar(Justificacion justificacion);
}

