package com.tallerjava.taller.controller;

import com.tallerjava.taller.dto.GuardarAsistenciasDTO;
import com.tallerjava.taller.dto.ActualizarAprendizDTO;
import com.tallerjava.taller.model.*;
import com.tallerjava.taller.service.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.web.csrf.CsrfToken;

import java.util.stream.Collectors;
import java.security.Principal;
import java.time.LocalDate;
import java.util.*;

@Controller
@RequestMapping("/instructor")
public class InstructorController {
    
    private final ISecurityService securityService;
    private final IUsuarioService usuarioService;
    private final IFichaService fichaService;
    private final IInstructorAsistenciaService instructorAsistenciaService;
    private final ICompetenciaService competenciaService;
    private final IJustificacionService justificacionService;
    private final IJornadaService jornadaService;
    
    @Autowired
    public InstructorController(ISecurityService securityService,
                              IUsuarioService usuarioService,
                              IFichaService fichaService,
                              IInstructorAsistenciaService instructorAsistenciaService,
                              ICompetenciaService competenciaService,
                              IJustificacionService justificacionService,
                              IJornadaService jornadaService) {
        this.securityService = securityService;
        this.usuarioService = usuarioService;
        this.fichaService = fichaService;
        this.instructorAsistenciaService = instructorAsistenciaService;
        this.competenciaService = competenciaService;
        this.justificacionService = justificacionService;
        this.jornadaService = jornadaService;
    }
    
    // ========== MÉTODO AUXILIAR PARA AGREGAR ATRIBUTOS COMUNES ==========
    private void agregarAtributosComunes(Principal principal, Model model, String activePage) {
        try {
            System.out.println("? ======= DEBUG - AUTENTICACIÓN =======");
            System.out.println("? Principal name: " + (principal != null ? principal.getName() : "null"));
            
            Integer idUsuario = securityService.getIdUsuarioActual();
            System.out.println("? ID Usuario desde securityService: " + idUsuario);
            
            if (idUsuario == null) {
                System.out.println("? ❌ ERROR: ID Usuario es NULL");
                return;
            }
            
            Usuario usuario = usuarioService.obtenerPorId(idUsuario);
            System.out.println("? Usuario encontrado:");
            System.out.println("? - ID: " + usuario.getIdUsuario());
            System.out.println("? - Nombre: " + usuario.getNombre() + " " + usuario.getApellido());
            
            // ✅ AGREGAR ATRIBUTOS NECESARIOS PARA EL LAYOUT
            model.addAttribute("usuario", usuario);
            model.addAttribute("activePage", activePage);
            
            System.out.println("? ✅ Atributos comunes agregados correctamente");
            
        } catch (Exception e) {
            System.err.println("? ❌ ERROR al agregar atributos comunes: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // ========== 1. GESTIONAR ASISTENCIA ==========
    @GetMapping("/gestionar-asistencia")
    public String gestionarAsistencia(@RequestParam(required = false) String ficha,
                                     @RequestParam(required = false) String jornada,
                                     @RequestParam(required = false) Integer fichaId,
                                     @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fechaAsistencia,
                                     Principal principal,
                                     Model model) {
        
        try {
            System.out.println("? ======= INSTRUCTOR - GESTIONAR ASISTENCIA =======");
            
            agregarAtributosComunes(principal, model, "gestionar-asistencia");
            
            Integer idUsuario = securityService.getIdUsuarioActual();
            Usuario instructor = usuarioService.obtenerPorId(idUsuario);
            
            List<Ficha> fichas = fichaService.obtenerFichasPorInstructorConFiltros(
                instructor.getIdUsuario(), ficha, jornada);

            List<Jornada> jornadas = jornadaService.obtenerTodasJornadas();
            
            List<Usuario> aprendices = new ArrayList<>();
            Ficha fichaActual = null;
            Map<Integer, String> asistenciasExistentes = new HashMap<>();
            
            if (fichaId != null) {
                fichaActual = fichas.stream()
                    .filter(f -> f.getIdFicha().equals(fichaId))
                    .findFirst()
                    .orElse(null);
                
                if (fichaActual != null) {
                    aprendices = usuarioService.obtenerAprendicesPorFicha(fichaId);
                    
                    LocalDate fecha = fechaAsistencia != null ? fechaAsistencia : LocalDate.now();
                    asistenciasExistentes = instructorAsistenciaService.obtenerAsistenciasPorFechaYFicha(
                        fecha, fichaId, instructor.getIdUsuario());
                }
            }
            
            model.addAttribute("instructor", instructor);
            model.addAttribute("fichas", fichas);
            model.addAttribute("jornadas", jornadas);
            model.addAttribute("fichaFilter", ficha);
            model.addAttribute("jornadaFilter", jornada);
            model.addAttribute("fichaSeleccionada", fichaId);
            model.addAttribute("aprendices", aprendices);
            model.addAttribute("fichaActual", fichaActual);
            model.addAttribute("fechaAsistencia", fechaAsistencia != null ? fechaAsistencia : LocalDate.now());
            model.addAttribute("asistenciasExistentes", asistenciasExistentes);
            
            return "instructor/gestionarAsistencia";
            
        } catch (Exception e) {
            System.err.println("? ❌ ERROR en gestionarAsistencia: " + e.getMessage());
            e.printStackTrace();
            
            agregarAtributosComunes(principal, model, "gestionar-asistencia");
            model.addAttribute("error", "Error al cargar la gestión de asistencia: " + e.getMessage());
            return "instructor/gestionarAsistencia";
        }
    }
    
    // ========== 2. ASISTENCIA FICHA ==========
    @GetMapping("/asistencia-ficha/{id}")
    public String asistenciaFicha(@PathVariable Integer id,
                                 @RequestParam(name = "fecha_asistencia", required = false) 
                                 @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fechaAsistencia,
                                 @RequestParam(name = "competencia_id", required = false) Integer competenciaId,
                                 @RequestParam(name = "editar_aprendiz", required = false) Integer editarAprendiz,
                                 Principal principal,
                                 Model model) {
        
        try {
            agregarAtributosComunes(principal, model, "gestionar-asistencia");
            
            Integer idUsuario = securityService.getIdUsuarioActual();
            Usuario instructor = usuarioService.obtenerPorId(idUsuario);
            
            Ficha ficha = fichaService.obtenerPorIdConAprendices(id);
            if (ficha == null) {
                throw new RuntimeException("Ficha no encontrada");
            }
            
            List<Competencia> competencias = new ArrayList<>();
            if (ficha.getIdPrograma() != null) {
                competencias = competenciaService.obtenerCompetenciasPorPrograma(ficha.getIdPrograma());
            } else {
                competencias = competenciaService.obtenerTodasCompetencias();
            }
            
            List<Usuario> aprendices = usuarioService.obtenerAprendicesPorFicha(id);
            
            Usuario aprendizEditar = null;
            if (editarAprendiz != null) {
                aprendizEditar = usuarioService.obtenerPorId(editarAprendiz);
            }
            
            LocalDate fecha = fechaAsistencia != null ? fechaAsistencia : LocalDate.now();
            Map<Integer, String> asistenciasExistentes = new HashMap<>();
            if (aprendices != null && !aprendices.isEmpty()) {
                asistenciasExistentes = instructorAsistenciaService.obtenerAsistenciasPorFechaYFicha(
                    fecha, id, instructor.getIdUsuario());
            }
            
            model.addAttribute("instructor", instructor);
            model.addAttribute("ficha", ficha);
            model.addAttribute("aprendices", aprendices);
            model.addAttribute("aprendizEditar", aprendizEditar);
            model.addAttribute("fechaAsistencia", fecha);
            model.addAttribute("competencias", competencias);
            model.addAttribute("competenciaId", competenciaId);
            model.addAttribute("asistenciasExistentes", asistenciasExistentes);
            
            return "instructor/asistenciaFicha";
            
        } catch (Exception e) {
            System.err.println("? ERROR en asistenciaFicha: " + e.getMessage());
            e.printStackTrace();
            
            agregarAtributosComunes(principal, model, "gestionar-asistencia");
            model.addAttribute("error", "Error al cargar la ficha: " + e.getMessage());
            return "instructor/asistenciaFicha";
        }
    }
    
    // ========== 3. GUARDAR ASISTENCIAS ==========
    @PostMapping("/guardar-asistencias")
    public String guardarAsistencias(
            @RequestParam("ficha_id") String fichaIdStr,
            @RequestParam("competencia_id") String competenciaIdStr,
            @RequestParam("fecha_asistencia") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fechaAsistencia,
            @RequestParam Map<String, String> allParams,
            HttpServletRequest request,
            HttpServletResponse response,
            RedirectAttributes redirectAttributes) {
        
        try {
            Integer fichaId = null;
            Integer competenciaId = null;
            
            if (fichaIdStr != null && !fichaIdStr.trim().isEmpty() && !"null".equalsIgnoreCase(fichaIdStr.trim())) {
                fichaId = Integer.parseInt(fichaIdStr.trim());
            }
            
            if (competenciaIdStr != null && !competenciaIdStr.trim().isEmpty() && !"null".equalsIgnoreCase(competenciaIdStr.trim())) {
                competenciaId = Integer.parseInt(competenciaIdStr.trim());
            }
            
            if (fichaId == null || competenciaId == null || fechaAsistencia == null) {
                redirectAttributes.addFlashAttribute("error", "Parámetros inválidos");
                return "redirect:/instructor/gestionar-asistencia?error=invalid_params";
            }
            
            Integer instructorId = securityService.getIdUsuarioActual();
            Usuario instructor = usuarioService.obtenerPorId(instructorId);
            
            GuardarAsistenciasDTO dto = new GuardarAsistenciasDTO();
            dto.setFichaId(fichaId);
            dto.setCompetenciaId(competenciaId);
            dto.setFechaAsistencia(fechaAsistencia);
            dto.setInstructorId(instructor.getIdUsuario());
            
            Map<Integer, String> asistenciasMap = new HashMap<>();
            allParams.forEach((key, value) -> {
                if (key.startsWith("asistencia[")) {
                    String idStr = key.substring("asistencia[".length(), key.length() - 1);
                    try {
                        Integer aprendizId = Integer.parseInt(idStr);
                        asistenciasMap.put(aprendizId, value);
                    } catch (NumberFormatException e) {
                        System.err.println("❌ Error al convertir ID de aprendiz: " + idStr);
                    }
                }
            });
            
            dto.setAsistencias(asistenciasMap);
            instructorAsistenciaService.guardarAsistencias(dto);
            
            redirectAttributes.addFlashAttribute("success", asistenciasMap.size() + " asistencias guardadas correctamente");
            
            return "redirect:/instructor/asistencia-ficha/" + fichaId + 
                   "?fecha_asistencia=" + fechaAsistencia + 
                   "&competencia_id=" + competenciaId + 
                   "&success=true";
            
        } catch (Exception e) {
            System.err.println("❌ ERROR al guardar asistencias: " + e.getMessage());
            e.printStackTrace();
            
            redirectAttributes.addFlashAttribute("error", "Error al guardar asistencias: " + e.getMessage());
            return "redirect:/instructor/gestionar-asistencia?error=server_error";
        }
    }
    
    // ========== 4. ACTUALIZAR APRENDIZ ==========
    @PostMapping("/actualizar-aprendiz/{id}")
    public String actualizarAprendiz(@PathVariable("id") Integer id,
                                    @RequestParam String cedula,
                                    @RequestParam String nombre,
                                    @RequestParam String apellido,
                                    @RequestParam String correo,
                                    @RequestParam(required = false) String telefono,
                                    @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fecha_actual,
                                    @RequestParam(name = "id_ficha", required = false) Integer idFicha,
                                    RedirectAttributes redirectAttributes,
                                    Principal principal) {
        
        try {
            if (id == null) {
                throw new IllegalArgumentException("ID del aprendiz es requerido");
            }
            
            ActualizarAprendizDTO dto = new ActualizarAprendizDTO();
            dto.setCedula(cedula);
            dto.setNombre(nombre);
            dto.setApellido(apellido);
            dto.setCorreo(correo);
            dto.setTelefono(telefono);
            
            usuarioService.actualizarAprendiz(id, dto);
            
            redirectAttributes.addFlashAttribute("success", "Aprendiz actualizado correctamente");
            
            if (idFicha != null) {
                String redirectUrl = "/instructor/asistencia-ficha/" + idFicha;
                if (fecha_actual != null) {
                    redirectUrl += "?fecha_asistencia=" + fecha_actual;
                }
                return "redirect:" + redirectUrl;
            } else {
                return "redirect:/instructor/gestionar-asistencia";
            }
            
        } catch (Exception e) {
            System.err.println("? ERROR al actualizar aprendiz: " + e.getMessage());
            e.printStackTrace();
            
            redirectAttributes.addFlashAttribute("error", "Error al actualizar aprendiz: " + e.getMessage());
            return "redirect:/instructor/gestionar-asistencia";
        }
    }
    
    // ========== 5. CONSULTAR ASISTENCIAS ==========
    @GetMapping("/consultar-asistencias")
    public String consultarAsistencias(
            @RequestParam(required = false) Integer ficha_id,
            @RequestParam(defaultValue = "rango") String modo_fecha,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fecha_inicio,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fecha_fin,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fecha_unica,
            Principal principal,
            Model model) {
        
        try {
            agregarAtributosComunes(principal, model, "consultar-asistencias");
            
            Integer instructorId = securityService.getIdUsuarioActual();
            Usuario instructor = usuarioService.obtenerPorId(instructorId);
            
            List<Ficha> fichas = fichaService.obtenerFichasPorInstructor(instructorId);
            
            List<Map<String, Object>> fichasConCount = new ArrayList<>();
            for (Ficha ficha : fichas) {
                Map<String, Object> fichaMap = new HashMap<>();
                fichaMap.put("ficha", ficha);
                
                Long aprendicesCount = usuarioService.contarAprendicesPorFicha(ficha.getIdFicha());
                fichaMap.put("aprendices_count", aprendicesCount != null ? aprendicesCount : 0);
                
                fichasConCount.add(fichaMap);
            }
            
            Map<String, Object> reporte = new HashMap<>();
            Ficha fichaSeleccionada = null;
            List<Map<String, Object>> resultados = new ArrayList<>();
            List<LocalDate> fechasConRegistros = new ArrayList<>();
            
            if (ficha_id != null) {
                fichaSeleccionada = fichas.stream()
                    .filter(f -> f.getIdFicha().equals(ficha_id))
                    .findFirst()
                    .orElse(null);
                
                if (fichaSeleccionada != null) {
                    LocalDate fechaInicioParam = fecha_inicio;
                    LocalDate fechaFinParam = fecha_fin;
                    
                    if ("dia".equals(modo_fecha) && fecha_unica != null) {
                        fechaInicioParam = fecha_unica;
                        fechaFinParam = fecha_unica;
                    }
                    
                    if (fechaInicioParam != null && fechaFinParam != null) {
                        reporte = instructorAsistenciaService.obtenerReporteAsistencias(
                            ficha_id, instructorId, fechaInicioParam, fechaFinParam);
                        
                        if (reporte != null) {
                            resultados = (List<Map<String, Object>>) reporte.get("resultados");
                            fechasConRegistros = (List<LocalDate>) reporte.get("fechasConRegistros");
                        }
                    }
                }
            }
            
            model.addAttribute("fichasConCount", fichasConCount);
            model.addAttribute("fichaSeleccionada", fichaSeleccionada);
            model.addAttribute("resultados", resultados != null ? resultados : new ArrayList<>());
            model.addAttribute("fechasConRegistros", fechasConRegistros != null ? fechasConRegistros : new ArrayList<>());
            model.addAttribute("fichaId", ficha_id);
            model.addAttribute("modoFecha", modo_fecha);
            model.addAttribute("fechaInicio", fecha_inicio);
            model.addAttribute("fechaFin", fecha_fin);
            model.addAttribute("fechaUnica", fecha_unica);
            
            return "instructor/consultarAsistencias";
            
        } catch (Exception e) {
            System.err.println("? ❌ ERROR en consultarAsistencias: " + e.getMessage());
            e.printStackTrace();
            
            agregarAtributosComunes(principal, model, "consultar-asistencias");
            model.addAttribute("error", "Error al consultar asistencias: " + e.getMessage());
            return "instructor/consultarAsistencias";
        }
    }
    
    // ========== 6. NOVEDADES APRENDICES ==========
    @GetMapping("/novedades-aprendices")
    public String novedadesAprendices(@RequestParam(required = false) Integer fichaId,
                                     @RequestParam(required = false) Long competenciaId,
                                     Principal principal,
                                     Model model) {
        
        try {
            System.out.println("🚀 ======= NOVEDADES APRENDICES - INICIANDO =======");
            
            agregarAtributosComunes(principal, model, "novedades-aprendices");
            
            Integer idUsuario = securityService.getIdUsuarioActual();
            Usuario instructor = usuarioService.obtenerPorId(idUsuario);
            
            List<Ficha> fichas = fichaService.obtenerFichasPorInstructor(instructor.getIdUsuario());
            List<Map<String, Object>> novedades = justificacionService
                .obtenerJustificacionesPendientesPorInstructor(
                    instructor.getIdUsuario(), fichaId, competenciaId);
            List<Competencia> competencias = competenciaService.obtenerTodasCompetencias();
            
            long novedadesCount = novedades.stream()
                .filter(n -> "Pendiente".equals(n.get("estado_justificacion")))
                .count();
            
            model.addAttribute("instructor", instructor);
            model.addAttribute("novedades", novedades);
            model.addAttribute("fichas", fichas);
            model.addAttribute("competencias", competencias);
            model.addAttribute("fichaFilter", fichaId);
            model.addAttribute("competenciaFilter", competenciaId);
            model.addAttribute("novedadesCount", novedadesCount);
            
            System.out.println("✅ Novedades cargadas: " + novedades.size());
            System.out.println("✅ NovedadesCount: " + novedadesCount);
            
            return "instructor/novedadesAprendices";
            
        } catch (Exception e) {
            System.err.println("❌ ERROR en novedadesAprendices: " + e.getMessage());
            e.printStackTrace();
            
            agregarAtributosComunes(principal, model, "novedades-aprendices");
            model.addAttribute("error", "Error al cargar novedades: " + e.getMessage());
            return "instructor/novedadesAprendices";
        }
    }
    
    // ========== 7. PROCESAR NOVEDAD ==========
@PostMapping("/procesar-novedad")
public String procesarNovedad(@RequestParam Long idJustificacion,
                             @RequestParam Long idAsistenciaAmbiente,
                             @RequestParam String accion,
                             @RequestParam(required = false) String observaciones,
                             Principal principal,
                             RedirectAttributes redirectAttributes) {
    
    try {
        System.out.println("? INSTRUCTOR - PROCESAR NOVEDAD - INICIANDO");
        System.out.println("? ID Justificación: " + idJustificacion);
        System.out.println("? ID Asistencia: " + idAsistenciaAmbiente);
        System.out.println("? Acción: " + accion);
        System.out.println("? Observaciones: " + observaciones);
        
        Integer idUsuario = securityService.getIdUsuarioActual();
        Usuario instructor = usuarioService.obtenerPorId(idUsuario);
        
        justificacionService.procesarJustificacion(
            idJustificacion, idAsistenciaAmbiente, accion, observaciones, instructor.getIdUsuario());
        
        redirectAttributes.addFlashAttribute("mensaje", 
            "Solicitud " + accion.toLowerCase() + " correctamente");
        redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        
        return "redirect:/instructor/novedades-aprendices?success";
        
    } catch (Exception e) {
        System.err.println("? ERROR al procesar novedad: " + e.getMessage());
        e.printStackTrace();
        
        redirectAttributes.addFlashAttribute("mensaje", "Error al procesar la solicitud: " + e.getMessage());
        redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        return "redirect:/instructor/novedades-aprendices?error";
    }
}
    
    // ========== 8. PERFIL INSTRUCTOR ==========
    @GetMapping("/perfil")
public String perfil(Principal principal, Model model) {
    
    try {
        System.out.println("📸 ======= INSTRUCTOR - PERFIL - INICIANDO =======");
        
        agregarAtributosComunes(principal, model, "perfil");
        
        Integer idUsuario = securityService.getIdUsuarioActual();
        Usuario instructor = usuarioService.obtenerPorId(idUsuario);
        
        // ✅ DEBUG: Agrega estos logs para verificar
        System.out.println("📸 ID Usuario: " + instructor.getIdUsuario());
        System.out.println("📸 Nombre: " + instructor.getNombre() + " " + instructor.getApellido());
        System.out.println("📸 Foto Perfil en BD: '" + instructor.getFotoPerfil() + "'");
        System.out.println("📸 Foto Perfil es null? " + (instructor.getFotoPerfil() == null));
        System.out.println("📸 Foto Perfil está vacío? " + (instructor.getFotoPerfil() != null && instructor.getFotoPerfil().isEmpty()));
        
        // También verifica qué devuelve el servicio
        String fotoPerfil = instructor.getFotoPerfil();
        if (fotoPerfil != null && !fotoPerfil.trim().isEmpty()) {
            System.out.println("✅ Foto encontrada: " + fotoPerfil);
        } else {
            System.out.println("❌ No hay foto de perfil en la BD para este usuario");
        }
        
        model.addAttribute("instructor", instructor);
        return "instructor/perfil";
        
    } catch (Exception e) {
        System.err.println("❌ ERROR en perfil instructor: " + e.getMessage());
        e.printStackTrace();
        
        agregarAtributosComunes(principal, model, "perfil");
        model.addAttribute("error", "Error al cargar el perfil: " + e.getMessage());
        return "instructor/perfil";
    }
}
    
    // ========== 9. ACTUALIZAR FOTO PERFIL ==========
    @PostMapping("/actualizar-foto-perfil")
    public String actualizarFotoPerfil(@RequestParam("foto_perfil") MultipartFile archivo,
                                      Principal principal,
                                      RedirectAttributes redirectAttributes) {
        
        try {
            System.out.println("? INSTRUCTOR - ACTUALIZAR FOTO PERFIL - INICIANDO");
            
            if (archivo.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Por favor seleccione una imagen");
                return "redirect:/instructor/perfil";
            }
            
            Integer idUsuario = securityService.getIdUsuarioActual();
            Usuario instructor = usuarioService.obtenerPorId(idUsuario);
            
            usuarioService.actualizarFotoPerfil(instructor.getIdUsuario(), archivo);
            
            redirectAttributes.addFlashAttribute("mensaje", "Foto de perfil actualizada correctamente");
            return "redirect:/instructor/perfil";
            
        } catch (Exception e) {
            System.err.println("? ERROR al actualizar foto perfil: " + e.getMessage());
            e.printStackTrace();
            
            redirectAttributes.addFlashAttribute("error", "Error al actualizar la foto: " + e.getMessage());
            return "redirect:/instructor/perfil";
        }
    }
    
    // ========== 0. ENDPOINT DE PRUEBA ==========
    @GetMapping("/debug-usuario")
    @ResponseBody
    public Map<String, Object> debugUsuario(Principal principal) {
        Map<String, Object> resultado = new HashMap<>();
        
        try {
            System.out.println("? ======= DEBUG USUARIO =======");
            
            resultado.put("principal_name", principal != null ? principal.getName() : "null");
            
            Integer idUsuario = securityService.getIdUsuarioActual();
            resultado.put("id_usuario_security", idUsuario);
            
            if (idUsuario != null) {
                Usuario usuario = usuarioService.obtenerPorId(idUsuario);
                
                Map<String, Object> usuarioMap = new HashMap<>();
                usuarioMap.put("id", usuario.getIdUsuario());
                usuarioMap.put("nombre", usuario.getNombre());
                usuarioMap.put("apellido", usuario.getApellido());
                usuarioMap.put("correo", usuario.getCorreo());
                
                if (usuario.getRoles() != null && !usuario.getRoles().isEmpty()) {
                    List<Map<String, Object>> rolesList = usuario.getRoles().stream()
                        .map(role -> {
                            Map<String, Object> roleMap = new HashMap<>();
                            roleMap.put("id", role.getId());
                            roleMap.put("name", role.getName());
                            roleMap.put("guard_name", role.getGuardName());
                            return roleMap;
                        })
                        .collect(Collectors.toList());
                    usuarioMap.put("roles", rolesList);
                } else {
                    usuarioMap.put("roles", Collections.emptyList());
                }
                
                resultado.put("usuario", usuarioMap);
                
                List<Ficha> fichas = fichaService.obtenerFichasPorInstructor(idUsuario);
                resultado.put("fichas_count", fichas.size());
                resultado.put("fichas", fichas.stream()
                    .map(f -> Map.of(
                        "id", f.getIdFicha(),
                        "numero", f.getNumeroFicha(),
                        "estado", f.getEstado(),
                        "jornada", f.getJornada() != null ? f.getJornada().getNombreJornada() : "null"
                    ))
                    .collect(Collectors.toList()));
            }
            
            System.out.println("? Resultado debug: " + resultado);
            
        } catch (Exception e) {
            resultado.put("error", e.getMessage());
            System.err.println("? ERROR en debug: " + e.getMessage());
            e.printStackTrace();
        }
        
        return resultado;
    }
}