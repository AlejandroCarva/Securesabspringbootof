package com.tallerjava.taller.controller;
import  com.tallerjava.taller.model.AsistenciaSede;
import com.tallerjava.taller.repository.UsuarioRepository;
import com.tallerjava.taller.model.Usuario;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalTime;
import java.time.LocalDate;
import com.tallerjava.taller.service.AsistenciaSedeService;
import com.tallerjava.taller.service.FichaService;
import com.tallerjava.taller.model.Ficha;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
@Controller
@RequestMapping("/coordinador/asistencia-sede")
public class AsistenciaSedeController {
    private final AsistenciaSedeService asistenciaSedeService;
    private final FichaService fichaService;
    private final UsuarioRepository usuarioRepository;
    public AsistenciaSedeController(AsistenciaSedeService asistenciaSedeService, FichaService fichaService, UsuarioRepository usuarioRepository) {
        this.asistenciaSedeService = asistenciaSedeService;
        this.fichaService = fichaService;
        this.usuarioRepository = usuarioRepository;
    }
    @GetMapping
    public String listarAsistencias(Model model) {
        List<AsistenciaSede> asistencias = asistenciaSedeService.listar();
        // Nombre esperado por la plantilla
        model.addAttribute("asistenciasSede", asistencias);
        // También enviamos la lista de fichas para poblar el select de filtro
        List<Ficha> fichas = fichaService.listar();
        model.addAttribute("listaFichas", fichas);
        // Enviar lista de usuarios para el formulario de creación (select)
        try {
            List<Usuario> usuarios = usuarioRepository.findAll();
            model.addAttribute("listaUsuarios", usuarios);
        } catch (Exception e) {
            model.addAttribute("listaUsuarios", java.util.Collections.emptyList());
        }

        return "coordinador/asistencia-sede";
    }

    @GetMapping("/debug")
    @ResponseBody
    public String debugAsistencias() {
        List<AsistenciaSede> asistencias = asistenciaSedeService.listar();
        StringBuilder sb = new StringBuilder();
        sb.append("Asistencias encontradas: ").append(asistencias != null ? asistencias.size() : 0).append("\n");
        if (asistencias != null) {
            int i = 0;
            for (AsistenciaSede a : asistencias) {
                sb.append("id=").append(a.getId())
                        .append(" usuario=").append(a.getUsuario() != null ? a.getUsuario().getCedula() : "N/D")
                        .append(" fecha=").append(a.getFecha())
                        .append(" horaEntrada=").append(a.getHoraEntrada() != null ? a.getHoraEntrada().toString() : "N/D")
                        .append(" horaSalida=").append(a.getHoraSalida() != null ? a.getHoraSalida().toString() : "N/D")
                        .append("\n");
                if (++i >= 50) break; // limitar salida
            }
        }
        return sb.toString();
    }

    @PostMapping("/guardar")
    public String guardarAsistencia(@RequestParam("usuarioId") Integer usuarioId,
                                    @RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
                                    @RequestParam(value = "horaEntrada", required = false) String horaEntradaStr,
                                    @RequestParam(value = "horaSalida", required = false) String horaSalidaStr,
                                    org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        try {
            AsistenciaSede a = new AsistenciaSede();
            Usuario u = usuarioRepository.findById(usuarioId).orElse(null);
            a.setUsuario(u);
            a.setFecha(fecha);
            if (horaEntradaStr != null && !horaEntradaStr.isBlank()) a.setHoraEntrada(LocalTime.parse(horaEntradaStr));
            if (horaSalidaStr != null && !horaSalidaStr.isBlank()) a.setHoraSalida(LocalTime.parse(horaSalidaStr));
            asistenciaSedeService.guardar(a);
            redirectAttributes.addFlashAttribute("success", "Asistencia registrada correctamente.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errors", java.util.List.of("No se pudo guardar la asistencia: " + ex.getMessage()));
        }
        return "redirect:/coordinador/asistencia-sede";
    }

}


