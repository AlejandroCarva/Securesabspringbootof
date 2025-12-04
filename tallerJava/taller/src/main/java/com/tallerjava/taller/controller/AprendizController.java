package com.tallerjava.taller.controller;

import com.tallerjava.taller.dto.AprendizAsistenciaDTO;
import com.tallerjava.taller.dto.JustificacionDTO;
import com.tallerjava.taller.model.Competencia;
import com.tallerjava.taller.model.Justificacion;
import com.tallerjava.taller.model.Usuario;
import com.tallerjava.taller.service.IAprendizAsistenciaService;
import com.tallerjava.taller.service.ICompetenciaService;
import com.tallerjava.taller.service.IJustificacionService;
import com.tallerjava.taller.service.ISecurityService;
import com.tallerjava.taller.service.IUsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/aprendiz")
public class AprendizController {
    
    private final ISecurityService securityService;
    private final IAprendizAsistenciaService aprendizAsistenciaService;
    private final IJustificacionService justificacionService;
    private final ICompetenciaService competenciaService;
    private final IUsuarioService usuarioService;
    
    @Autowired
    public AprendizController(ISecurityService securityService,
                            IAprendizAsistenciaService aprendizAsistenciaService,
                            IJustificacionService justificacionService,
                            ICompetenciaService competenciaService,
                            IUsuarioService usuarioService) {
        this.securityService = securityService;
        this.aprendizAsistenciaService = aprendizAsistenciaService;
        this.justificacionService = justificacionService;
        this.competenciaService = competenciaService;
        this.usuarioService = usuarioService;
    }
    
    @GetMapping("/asistencia")
    public String consultarAsistencia(@RequestParam(required = false) String competencia,
                                    @RequestParam(required = false) String fecha,
                                    Principal principal,
                                    Model model) {
        
        try {
            System.out.println("? CONTROLLER ASISTENCIA - INICIANDO");
            String cedulaUsuario = principal.getName();
            System.out.println("? Usuario autenticado: " + cedulaUsuario);
            
            // Obtener usuario usando el ID del usuario actual
            Integer idUsuario = securityService.getIdUsuarioActual();
            System.out.println("? ID Usuario obtenido: " + idUsuario);
            
            Usuario usuario = usuarioService.obtenerPorId(idUsuario.longValue());
            System.out.println("? Usuario encontrado: " + usuario.getNombre() + " " + usuario.getApellido());
            
            // ✅ CORREGIDO: Usa el servicio temporal (lista vacía)
            List<AprendizAsistenciaDTO> asistencias = aprendizAsistenciaService
                .obtenerAsistenciasPorFiltros(usuario.getIdUsuario(), competencia, fecha);
            
            System.out.println("? Asistencias obtenidas: " + asistencias.size());
            
            model.addAttribute("usuario", usuario);
            model.addAttribute("asistencias", asistencias);
            
            return "aprendiz/consultarAsistencia";
            
        } catch (Exception e) {
            System.err.println("? ERROR en consultarAsistencia: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Error al cargar las asistencias: " + e.getMessage());
            return "aprendiz/consultarAsistencia";
        }
    }
    
    // ✅ LOS DEMÁS MÉTODOS SE MANTIENEN IGUAL
    @GetMapping("/radicar-justificacion")
    public String mostrarFormularioJustificacion(Principal principal, Model model) {
        
        try {
            System.out.println("? CONTROLLER - RADICAR JUSTIFICACIÓN (MOSTRAR)");
            
            // Obtener usuario usando el ID del usuario actual
            Integer idUsuario = securityService.getIdUsuarioActual();
            Usuario usuario = usuarioService.obtenerPorId(idUsuario.longValue());
            
            List<Competencia> competencias = competenciaService.obtenerTodasCompetencias();
            
            System.out.println("? Competencias encontradas: " + competencias.size());
            
            model.addAttribute("usuario", usuario);
            model.addAttribute("competencias", competencias);
            
            // ✅ IMPORTANTE: Siempre agregar el DTO (incluso si ya viene con errores)
            if (!model.containsAttribute("justificacion")) {
                model.addAttribute("justificacion", new JustificacionDTO());
            }
            
            return "aprendiz/radicarJustificacion";
            
        } catch (Exception e) {
            System.err.println("? ERROR en mostrarFormularioJustificacion: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Error al cargar el formulario: " + e.getMessage());
            return "aprendiz/radicarJustificacion";
        }
    }
    
    // ✅ RECIBIR FORMULARIO (CORREGIDO)
    @PostMapping("/guardar-justificacion")
    public String guardarJustificacion(
            @Valid @ModelAttribute("justificacion") JustificacionDTO justificacionDTO,
            BindingResult result,
            @RequestParam("evidencia") MultipartFile evidencia,
            @RequestParam("competencias") List<String> competencias,
            Principal principal,
            RedirectAttributes redirectAttributes) {
        
        System.out.println("? CONTROLLER - GUARDAR JUSTIFICACIÓN (RECIBIR)");
        
        if (result.hasErrors()) {
            System.err.println("? ERRORES de validación en el formulario:");
            result.getFieldErrors().forEach(error -> 
                System.err.println("? - " + error.getField() + ": " + error.getDefaultMessage())
            );
            
            // ✅ IMPORTANTE: Mantener competencias para cuando hay errores
            try {
                Integer idUsuario = securityService.getIdUsuarioActual();
                Usuario usuario = usuarioService.obtenerPorId(idUsuario.longValue());
                List<Competencia> competenciasList = competenciaService.obtenerTodasCompetencias();
                redirectAttributes.addFlashAttribute("competencias", competenciasList);
                redirectAttributes.addFlashAttribute("usuario", usuario);
            } catch (Exception e) {
                System.err.println("? Error al cargar competencias para errores: " + e.getMessage());
            }
            
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.justificacion", result);
            redirectAttributes.addFlashAttribute("justificacion", justificacionDTO);
            return "redirect:/aprendiz/radicar-justificacion?error=validation";
        }
        
        try {
            if (evidencia.isEmpty()) {
                throw new IllegalArgumentException("El archivo de evidencia es obligatorio");
            }
            
            System.out.println("? Archivo recibido: " + evidencia.getOriginalFilename());
            System.out.println("? Competencias recibidas: " + competencias);
            
            // Obtener usuario usando el ID del usuario actual
            Integer idUsuario = securityService.getIdUsuarioActual();
            Usuario usuario = usuarioService.obtenerPorId(idUsuario.longValue());
            
            System.out.println("? Guardando justificación para usuario: " + usuario.getNombre());
            
            justificacionService.guardarJustificacion(justificacionDTO, evidencia, usuario, competencias);
            
            redirectAttributes.addFlashAttribute("mensaje", "Solicitud enviada correctamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            return "redirect:/aprendiz/radicar-justificacion?success";
            
        } catch (Exception e) {
            System.err.println("? ERROR al guardar justificación: " + e.getMessage());
            e.printStackTrace();
            
            // ✅ Cargar competencias también para errores de guardado
            try {
                Integer idUsuario = securityService.getIdUsuarioActual();
                Usuario usuario = usuarioService.obtenerPorId(idUsuario.longValue());
                List<Competencia> competenciasList = competenciaService.obtenerTodasCompetencias();
                redirectAttributes.addFlashAttribute("competencias", competenciasList);
                redirectAttributes.addFlashAttribute("usuario", usuario);
            } catch (Exception ex) {
                System.err.println("? Error al cargar competencias para errores de guardado: " + ex.getMessage());
            }
            
            redirectAttributes.addFlashAttribute("mensaje", "Error al enviar la solicitud: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
            redirectAttributes.addFlashAttribute("justificacion", justificacionDTO);
            return "redirect:/aprendiz/radicar-justificacion?error";
        }
    }
    
    @GetMapping("/estado-justificaciones")
    public String estadoJustificaciones(Principal principal, Model model) {
        
        try {
            System.out.println("? CONTROLLER - ESTADO JUSTIFICACIONES");
            
            // Obtener usuario usando el ID del usuario actual
            Integer idUsuario = securityService.getIdUsuarioActual();
            Usuario usuario = usuarioService.obtenerPorId(idUsuario.longValue());
            
            List<Justificacion> justificaciones = justificacionService.obtenerJustificacionesPorUsuario(usuario.getIdUsuario());
            
            System.out.println("? Justificaciones encontradas: " + justificaciones.size());
            
            model.addAttribute("usuario", usuario);
            model.addAttribute("justificaciones", justificaciones);
            
            return "aprendiz/estadoJustificacion";
            
        } catch (Exception e) {
            System.err.println("? ERROR en estadoJustificaciones: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Error al cargar las justificaciones: " + e.getMessage());
            return "aprendiz/estadoJustificacion";
        }
    }
    
    @GetMapping("/perfil")
    public String perfil(Principal principal, Model model) {
        
        try {
            System.out.println("? CONTROLLER - PERFIL");
            
            // Obtener usuario usando el ID del usuario actual
            Integer idUsuario = securityService.getIdUsuarioActual();
            Usuario usuario = usuarioService.obtenerPorId(idUsuario.longValue());
            
            model.addAttribute("usuario", usuario);
            return "aprendiz/perfil";
            
        } catch (Exception e) {
            System.err.println("? ERROR en perfil: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Error al cargar el perfil: " + e.getMessage());
            return "aprendiz/perfil";
        }
    }

    @PostMapping("/actualizarPerfil")
public String actualizarPerfil(@RequestParam("foto_perfil") MultipartFile archivo,
                              Principal principal,
                              RedirectAttributes redirectAttributes) {
    
    try {
        System.out.println("? CONTROLLER - ACTUALIZAR PERFIL");
        
        if (archivo.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Por favor seleccione una imagen");
            return "redirect:/aprendiz/perfil";
        }
        
        // Obtener usuario
        Integer idUsuario = securityService.getIdUsuarioActual();
        Usuario usuario = usuarioService.obtenerPorId(idUsuario.longValue());
        
        // Lógica para guardar la foto (implementar en usuarioService)
        usuarioService.actualizarFotoPerfil(usuario.getIdUsuario(), archivo);
        
        redirectAttributes.addFlashAttribute("mensaje", "Foto de perfil actualizada correctamente");
        return "redirect:/aprendiz/perfil";
        
    } catch (Exception e) {
        System.err.println("? ERROR al actualizar perfil: " + e.getMessage());
        e.printStackTrace();
        redirectAttributes.addFlashAttribute("error", "Error al actualizar la foto: " + e.getMessage());
        return "redirect:/aprendiz/perfil";
    }
    }
}