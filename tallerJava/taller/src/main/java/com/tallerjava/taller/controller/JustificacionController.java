package com.tallerjava.taller.controller;

import com.tallerjava.taller.model.Justificacion;
import com.tallerjava.taller.service.IJustificacionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/coordinador/justificaciones")
public class JustificacionController {

    private final IJustificacionService justificacionService;

    public JustificacionController(IJustificacionService justificacionService) {
        this.justificacionService = justificacionService;
    }

    @GetMapping
    public String listarJustificaciones(Model model) {
        List<Justificacion> justificaciones = justificacionService.listar();
        model.addAttribute("justificaciones", justificaciones);
        return "coordinador/justificaciones";
    }
}
