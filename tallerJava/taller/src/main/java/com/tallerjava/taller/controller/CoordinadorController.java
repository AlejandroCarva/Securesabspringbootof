package com.tallerjava.taller.controller;


import com.tallerjava.taller.model.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;

import com.tallerjava.taller.repository.FichaRepository;
import com.tallerjava.taller.repository.UsuarioRepository;
import com.tallerjava.taller.repository.AsistenciaSedeRepository;
import com.tallerjava.taller.repository.JustificacionRepository;
import com.tallerjava.taller.repository.NovedadRepository;

@Controller
public class CoordinadorController {

    private final FichaRepository fichaRepository;
    private final UsuarioRepository usuarioRepository;
    private final AsistenciaSedeRepository asistenciaSedeRepository;
    private final JustificacionRepository justificacionRepository;
    private final NovedadRepository novedadRepository;

    public CoordinadorController(FichaRepository fichaRepository,
                                 UsuarioRepository usuarioRepository,
                                 AsistenciaSedeRepository asistenciaSedeRepository,
                                 JustificacionRepository justificacionRepository,
                                 NovedadRepository novedadRepository) {
        this.fichaRepository = fichaRepository;
        this.usuarioRepository = usuarioRepository;
        this.asistenciaSedeRepository = asistenciaSedeRepository;
        this.justificacionRepository = justificacionRepository;
        this.novedadRepository = novedadRepository;
    }

    @GetMapping("/coordinador")
    public String inicio(HttpSession session, Model model) {
        // Obtener el usuario de la sesión
        Usuario usuarioCoordinador = (Usuario) session.getAttribute("usuarioCoordinador");

        // Populate common dashboard metrics so the /coordinador page reflects current state
        long totalFichas = fichaRepository.count();
        int totalAprendices = usuarioRepository.findByRoleNameIgnoreCase("aprendiz").size();

        // Count presentes today in sede (entries for today with horaEntrada != null)
        LocalDate today = LocalDate.now();
        int presentesSedeHoy = (int) asistenciaSedeRepository.findAll().stream()
                .filter(a -> a.getFecha() != null && a.getFecha().equals(today) && a.getHoraEntrada() != null)
                .count();

        // Count justificaciones pendientes (estado == null or 'Pendiente')
        int justPendientes = (int) justificacionRepository.findAll().stream()
                .filter(j -> j.getEstado() == null || "Pendiente".equalsIgnoreCase(j.getEstado()))
                .count();

        model.addAttribute("totalFichas", totalFichas);
        model.addAttribute("totalAprendices", totalAprendices);
        model.addAttribute("presentesSedeHoy", presentesSedeHoy);
        model.addAttribute("justPendientes", justPendientes);

        // Add listaFichas so fragments that expect it can render without extra request
        model.addAttribute("listaFichas", fichaRepository.findAll());

        // Pasar el usuario coordinador desde la sesión
        model.addAttribute("usuarioCoordinador", usuarioCoordinador);

        return "coordinador/inicio";
    }

    @GetMapping("/coordinador/crear-novedad")
    public String redirigirCrearNovedad(HttpSession session, Model model) {
        try {
            // Añadir datos necesarios para la vista crear-novedad
            model.addAttribute("listaFichas", fichaRepository.findAll());

            // Obtener instructores (role name 'instructor')
            try {
                model.addAttribute("listaInstructores", usuarioRepository.findByRoleNameIgnoreCase("instructor"));
            } catch (Exception e) {
                model.addAttribute("listaInstructores", java.util.Collections.emptyList());
            }

            // Pasar lista de novedades si el repositorio está disponible
            try {
                if (this.novedadRepository != null) {
                    java.util.List<com.tallerjava.taller.model.Novedad> novs = this.novedadRepository.findAll();
                    if (novs == null || novs.isEmpty()) {
                        com.tallerjava.taller.model.Novedad demo1 = new com.tallerjava.taller.model.Novedad();
                        demo1.setId(-1);
                        demo1.setTitulo("Demo: Corte de agua en laboratorio");
                        demo1.setDescripcion("Se programó un corte de agua que afecta las prácticas del grupo.");
                        demo1.setFecha(java.time.LocalDate.now());
                        demo1.setEstado("Pendiente");

                        com.tallerjava.taller.model.Novedad demo2 = new com.tallerjava.taller.model.Novedad();
                        demo2.setId(-2);
                        demo2.setTitulo("Demo: Nuevo comunicado de seguridad");
                        demo2.setDescripcion("Recordatorio: uso obligatorio de EPP durante las prácticas.");
                        demo2.setFecha(java.time.LocalDate.now().minusDays(1));
                        demo2.setEstado("En proceso");

                        novs = new java.util.ArrayList<>();
                        novs.add(demo1);
                        novs.add(demo2);
                    }
                    model.addAttribute("listaNovedades", novs);
                } else {
                    model.addAttribute("listaNovedades", java.util.Collections.emptyList());
                }
            } catch (Exception e) {
                model.addAttribute("listaNovedades", java.util.Collections.emptyList());
            }

            // Pasar usuario coordinador si está en sesión
            Usuario usuarioCoordinador = (Usuario) session.getAttribute("usuarioCoordinador");
            model.addAttribute("usuarioCoordinador", usuarioCoordinador);

            return "coordinador/crear-novedad";
        } catch (Exception ex) {
            // Registrar la excepción y devolver una vista segura para evitar response committed
            ex.printStackTrace();
            model.addAttribute("errorMensaje", "Ocurrió un error al preparar la vista: " + ex.getMessage());
            // devolver una vista alternativa segura para mostrar un mensaje simple
            return "coordinador/crear-novedad-safe";
        }
    }

    // La ruta /coordinador/justificaciones la maneja `JustificacionController`
    // para evitar mapeos duplicados quitamos el método simple que devolvía la vista.
    @GetMapping("/coordinador/perfil")
    public String perfil(HttpSession session, Model model) {
        // Obtener el usuario de la sesión
        Usuario usuarioCoordinador = (Usuario) session.getAttribute("usuarioCoordinador");
        model.addAttribute("usuarioCoordinador", usuarioCoordinador);
        return "coordinador/mi-perfil";
    }

    @GetMapping("/coordinador/novedades/editar/{id}")
    public String editarNovedadPorId(@org.springframework.web.bind.annotation.PathVariable("id") Integer id, HttpSession session, Model model) {
        try {
            if (this.novedadRepository != null && id != null) {
                java.util.Optional<com.tallerjava.taller.model.Novedad> opt = this.novedadRepository.findById(id);
                if (opt.isPresent()) {
                    com.tallerjava.taller.model.Novedad n = opt.get();
                    model.addAttribute("novedad", n);
                } else {
                    model.addAttribute("novedad", null);
                    model.addAttribute("error", "No se encontró la novedad solicitada.");
                }
            } else {
                model.addAttribute("novedad", null);
                model.addAttribute("error", "Repositorio de novedades no disponible.");
            }
        } catch (Exception e) {
            model.addAttribute("novedad", null);
            model.addAttribute("error", "Error al cargar la novedad: " + e.getMessage());
        }

        // Añadir datos necesarios para los selects (fichas e instructores)
        model.addAttribute("listaFichas", fichaRepository.findAll());
        try { model.addAttribute("listaInstructores", usuarioRepository.findByRoleNameIgnoreCase("instructor")); } catch (Exception ex) { model.addAttribute("listaInstructores", java.util.Collections.emptyList()); }

        // usuario coordinador
        Usuario usuarioCoordinador = (Usuario) session.getAttribute("usuarioCoordinador");
        model.addAttribute("usuarioCoordinador", usuarioCoordinador);

        return "coordinador/editar-novedad";
    }
}
