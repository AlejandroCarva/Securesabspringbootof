package com.tallerjava.taller.controller;

import  com.tallerjava.taller.model.AsistenciaAmbiente;
import com.tallerjava.taller.service.AsistenciaAmbienteService;
import com.tallerjava.taller.service.FichaService;
import com.tallerjava.taller.model.Ficha;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
@Controller
@RequestMapping("/coordinador/asistencia-ambiente")
public class AsistenciaAmbienteController {
    private final AsistenciaAmbienteService asistenciaAmbienteService;
    private final FichaService fichaService;

    public AsistenciaAmbienteController(AsistenciaAmbienteService asistenciaAmbienteService, FichaService fichaService) {
        this.asistenciaAmbienteService = asistenciaAmbienteService;
        this.fichaService = fichaService;
    }

    @GetMapping
    public String listarAsistencias(Model model) {
        List<AsistenciaAmbiente> asistencias = asistenciaAmbienteService.listar();
        // Nombre esperado por la plantilla
        model.addAttribute("asistenciasAmbiente", asistencias);
        // También enviamos la lista de fichas para poblar el select de filtro
        List<Ficha> fichas = fichaService.listar();
        model.addAttribute("listaFichas", fichas);
        return "coordinador/asistencia-ambiente";
    }

}
