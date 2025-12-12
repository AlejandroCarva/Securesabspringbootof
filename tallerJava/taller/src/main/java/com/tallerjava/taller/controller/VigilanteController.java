package com.tallerjava.taller.controller;

import com.tallerjava.taller.dto.RegistroManualDTO;
import com.tallerjava.taller.dto.RegistroManualForm;
import com.tallerjava.taller.model.AsistenciaSede;
import com.tallerjava.taller.model.Usuario;
import com.tallerjava.taller.model.Visitante;
import com.tallerjava.taller.model.RegistroManual;

import com.tallerjava.taller.repository.AsistenciaSedeRepository;
import com.tallerjava.taller.repository.RegistroManualRepository;
import com.tallerjava.taller.repository.UsuarioRepository;
import com.tallerjava.taller.repository.VisitanteRepository;

import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Controller
@RequestMapping("/vigilante")
public class VigilanteController {

    private final UsuarioRepository usuarioRepository;
    private final RegistroManualRepository registroManualRepository;
    private final VisitanteRepository visitanteRepository;
    private final AsistenciaSedeRepository asistenciaSedeRepository;

    public VigilanteController(UsuarioRepository usuarioRepository,
                               RegistroManualRepository registroManualRepository,
                               VisitanteRepository visitanteRepository,
                               AsistenciaSedeRepository asistenciaSedeRepository) {
        this.usuarioRepository = usuarioRepository;
        this.registroManualRepository = registroManualRepository;
        this.visitanteRepository = visitanteRepository;
        this.asistenciaSedeRepository = asistenciaSedeRepository;
    }

    // Helper: añade el usuario autenticado al modelo si está presente
    private void addUsuarioToModel(Model model, Authentication authentication) {
        try {
            if (authentication == null) return;
            String cedula = authentication.getName();
            if (cedula == null) return;
            Optional<Usuario> usuarioOpt = usuarioRepository.findByCedula(cedula);
            usuarioOpt.ifPresent(usuario -> model.addAttribute("usuario", usuario));
        } catch (Exception ignored) {
            // No fallar si algo va mal al obtener usuario
        }
    }

    // ============================================================
    //   MÉTODO AUXILIAR: HISTORIAL REGISTRO MANUAL
    // ============================================================
    private void cargarHistorialRegistroManual(Model model, int page) {

        int pageSize = 10;

        Pageable pageable = PageRequest.of(
                page,
                pageSize,
                Sort.by("fecha").descending().and(Sort.by("hora").descending())
        );

        Page<RegistroManual> registrosPage = registroManualRepository.findAll(pageable);
        List<RegistroManual> registrosList = registrosPage.getContent();

        // 🔄 AGRUPAR REGISTROS POR DOCUMENTO/USUARIO
        Map<String, RegistroManualDTO> registrosAgrupados = new LinkedHashMap<>();

        for (RegistroManual r : registrosList) {
            String key = r.getDocumento(); // Clave única por documento

            Optional<Usuario> uOpt = usuarioRepository.findByCedula(r.getDocumento());
            String nombre = uOpt.map(Usuario::getNombre).orElse("Desconocido");
            String apellido = uOpt.map(Usuario::getApellido).orElse("");

            if (registrosAgrupados.containsKey(key)) {
                // Ya existe, actualizar entrada o salida
                RegistroManualDTO dto = registrosAgrupados.get(key);
                if ("Ingreso".equals(r.getTipoMovimiento())) {
                    dto.setHoraEntrada(r.getHora());
                    dto.setFecha(r.getFecha());
                } else if ("Salida".equals(r.getTipoMovimiento())) {
                    dto.setHoraSalida(r.getHora());
                }
                dto.setMotivo(r.getMotivo());
            } else {
                // Crear nuevo DTO
                LocalTime horaEntrada = null;
                LocalTime horaSalida = null;

                if ("Ingreso".equals(r.getTipoMovimiento())) {
                    horaEntrada = r.getHora();
                } else if ("Salida".equals(r.getTipoMovimiento())) {
                    horaSalida = r.getHora();
                }

                RegistroManualDTO dto = new RegistroManualDTO(
                        r.getDocumento(),
                        nombre,
                        apellido,
                        r.getMotivo(),
                        r.getFecha(),
                        horaEntrada,
                        horaSalida
                );

                registrosAgrupados.put(key, dto);
            }
        }

        // Convertir a lista manteniendo el orden
        List<RegistroManualDTO> registrosFinal = new ArrayList<>(registrosAgrupados.values());

        int totalPages = registrosPage.getTotalPages();
        List<Integer> pageNumbers = new ArrayList<>();
        for (int i = 0; i < totalPages; i++) pageNumbers.add(i);

        model.addAttribute("registrosList", registrosFinal);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageNumbers", pageNumbers);
        model.addAttribute("currentPage", page);
    }

    // ============================================================
    //   GET: FORMULARIO DE REGISTRO MANUAL
    // ============================================================
    @GetMapping("/registro-manual")
    public String mostrarFormularioRegistroManual(
            @RequestParam(name = "page", defaultValue = "0") int page,
            Model model,
            Authentication authentication) {

        // Añadir usuario al modelo para el sidebar
        addUsuarioToModel(model, authentication);

        model.addAttribute("registroManualForm", new RegistroManualForm());
        model.addAttribute("fechaHoy", LocalDate.now());
        model.addAttribute("horaAhora", LocalTime.now());

        cargarHistorialRegistroManual(model, page);

        return "vigilante/registro-manual";
    }

    // ============================================================
    //   POST: REGISTRO MANUAL
    // ============================================================
    @PostMapping("/registro-manual")
    public String registrarManual(
            @Valid @ModelAttribute("registroManualForm") RegistroManualForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model,
            Authentication authentication) {

        // Añadir usuario al modelo para el caso de errores que re-muestren la vista
        addUsuarioToModel(model, authentication);

        if (bindingResult.hasErrors()) {
            model.addAttribute("fechaHoy", LocalDate.now());
            model.addAttribute("horaAhora", LocalTime.now());
            cargarHistorialRegistroManual(model, 0);
            return "vigilante/registro-manual";
        }

        Optional<Usuario> usuarioOpt = usuarioRepository.findByCedula(form.getDocumento());

        if (usuarioOpt.isEmpty()) {
            bindingResult.rejectValue(
                    "documento",
                    "documento.noRegistrado",
                    "Este documento NO pertenece a un usuario registrado."
            );
            model.addAttribute("fechaHoy", LocalDate.now());
            model.addAttribute("horaAhora", LocalTime.now());
            cargarHistorialRegistroManual(model, 0);
            return "vigilante/registro-manual";
        }

        // ✅ VALIDACIÓN: Verificar el ÚLTIMO registro del día
        Optional<RegistroManual> ultimoRegistro = registroManualRepository
                .findLastRegistroByDocumentoAndFecha(
                        form.getDocumento(),
                        LocalDate.now()
                );

        if (ultimoRegistro.isPresent()) {
            String ultimoTipo = ultimoRegistro.get().getTipoMovimiento();
            String nuevoTipo = form.getTipoMovimiento();

            if (ultimoTipo.equals(nuevoTipo)) {
                bindingResult.rejectValue(
                        "documento",
                        "registro.duplicado",
                        "No puede registrar dos movimientos de " + nuevoTipo +
                                " seguidos. Debe registrar una " +
                                (nuevoTipo.equals("Ingreso") ? "Salida" : "Ingreso") +
                                " primero."
                );
                model.addAttribute("fechaHoy", LocalDate.now());
                model.addAttribute("horaAhora", LocalTime.now());
                cargarHistorialRegistroManual(model, 0);
                return "vigilante/registro-manual";
            }
        }

        // Obtener el usuario autenticado
        String cedula = authentication.getName();
        Optional<Usuario> usuarioAutenticadoOpt = usuarioRepository.findByCedula(cedula);

        if (usuarioAutenticadoOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Usuario no encontrado.");
            return "redirect:/vigilante/registro-manual";
        }

        Usuario usuarioAutenticado = usuarioAutenticadoOpt.get();

        // ✅ Crear nuevo registro (Ingreso o Salida)
        RegistroManual registro = new RegistroManual();
        registro.setDocumento(form.getDocumento());
        registro.setTipoMovimiento(form.getTipoMovimiento());
        registro.setMotivo(form.getMotivo());
        registro.setFecha(LocalDate.now());
        registro.setHora(LocalTime.now());
        registro.setIdUsuario(usuarioAutenticado.getIdUsuario());

        registroManualRepository.save(registro);

        redirectAttributes.addFlashAttribute("success", "Registro manual creado correctamente.");
        return "redirect:/vigilante/registro-manual";
    }

    // ============================================================
    //   GET: FORMULARIO REGISTRAR INVITADOS
    // ============================================================
    @GetMapping("/registrar-invitados")
    public String mostrarFormularioInvitados(Model model) {

        model.addAttribute("visitante", new Visitante());
        model.addAttribute("fechaHoy", LocalDate.now());
        model.addAttribute("horaAhora", LocalTime.now());

        // 👇 ID del vigilante (por ahora fijo; DEBE existir en la tabla usuarios)
        model.addAttribute("idUsuarioVigilante", 1L);

        return "vigilante/registrar-invitados";
    }

    // ============================================================
    //   POST: REGISTRAR INVITADOS (crea asistencia_sede)
    // ============================================================
    @PostMapping("/registrar-invitados")
    public String registrarInvitado(
            @Valid @ModelAttribute("visitante") Visitante visitante,
            BindingResult result,
            @RequestParam(name = "idUsuario", required = false) Long idUsuario,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("fechaHoy", LocalDate.now());
            model.addAttribute("horaAhora", LocalTime.now());
            return "vigilante/registrar-invitados";
        }

        // 🔎 Buscar si ya existe un visitante con esa cédula
        String cedulaStr = visitante.getCedula() != null ? visitante.getCedula() : "";
        Optional<Visitante> existenteOpt = visitanteRepository.findByCedula(cedulaStr);

        if (existenteOpt.isPresent()) {
            Visitante existente = existenteOpt.get();
            AsistenciaSede asistenciaExistente = existente.getAsistenciaSede();

            // 🚫 Regla: no permitir NUEVA entrada si tiene una asistencia sin salida
            if (asistenciaExistente != null && asistenciaExistente.getHoraSalida() == null) {
                result.rejectValue(
                        "cedula",
                        "cedula.abierta",
                        "Este invitado ya tiene una entrada registrada y NO tiene salida de la sede."
                );
                model.addAttribute("fechaHoy", LocalDate.now());
                model.addAttribute("horaAhora", LocalTime.now());
                return "vigilante/registrar-invitados";
            }
        }

        // Si no llega el idUsuario desde el formulario, ponemos uno fijo (que exista en usuarios)
        if (idUsuario == null) {
            idUsuario = 1L;
        }

        // ✅ Crear SIEMPRE una nueva asistencia_sede (nueva entrada)
        AsistenciaSede asistencia = new AsistenciaSede();
        asistencia.setFecha(LocalDate.now());
        asistencia.setHoraEntrada(LocalTime.now());
        // Buscar el usuario por ID
        Usuario usuario = usuarioRepository.findById(idUsuario.intValue()).orElse(null);
        asistencia.setUsuario(usuario);

        asistencia = asistenciaSedeRepository.save(asistencia);

        // ✅ Asociar el visitante a esta nueva asistencia
        if (existenteOpt.isPresent()) {
            // Reutilizamos el visitante existente (no violamos UNIQUE cedula)
            Visitante existente = existenteOpt.get();
            existente.setNombre(visitante.getNombre());
            existente.setApellido(visitante.getApellido());
            existente.setMotivo(visitante.getMotivo());
            existente.setAsistenciaSede(asistencia);

            visitanteRepository.save(existente);
        } else {
            // Invitado nuevo
            visitante.setAsistenciaSede(asistencia);
            visitanteRepository.save(visitante);
        }

        redirectAttributes.addFlashAttribute("success", "Invitado registrado correctamente.");
        return "redirect:/vigilante/registrar-invitados";
    }

    // ============================================================
    //   POST: REGISTRAR SALIDA DESDE CONSULTAR INVITADOS
    // ============================================================
    @PostMapping("/salida-invitado/{id}")
    public String registrarSalidaInvitadoDesdeListado(
            @PathVariable("id") Long idVisitante,
            RedirectAttributes redirectAttributes) {

        Optional<Visitante> visitanteOpt = visitanteRepository.findById(idVisitante);

        if (visitanteOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "El invitado no existe.");
            return "redirect:/vigilante/consultar-invitados";
        }

        Visitante visitante = visitanteOpt.get();
        AsistenciaSede asistencia = visitante.getAsistenciaSede();

        // Validar que tenga una entrada abierta
        if (asistencia == null || asistencia.getHoraSalida() != null) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    "Este invitado no tiene una entrada activa o ya tiene registrada una salida."
            );
            return "redirect:/vigilante/consultar-invitados";
        }

        // Marcar la salida
        asistencia.setHoraSalida(LocalTime.now());

        asistenciaSedeRepository.save(asistencia);

        redirectAttributes.addFlashAttribute(
                "success",
                "Salida registrada correctamente para el invitado con cédula " + visitante.getCedula() + "."
        );

        return "redirect:/vigilante/consultar-invitados";
    }

    // ============================================================
    //   GET: CONSULTAR INVITADOS (CON FILTROS Y BOTÓN DE SALIDA)
    // ============================================================
    // ============================================================
//   GET: CONSULTAR INVITADOS (CON FILTROS Y BOTÓN DE SALIDA)
// ============================================================
    @GetMapping("/consultar-invitados")
    public String consultarInvitados(
            @RequestParam(name = "nombre", required = false) String nombre,
            @RequestParam(name = "cedula", required = false) String cedula,
            @RequestParam(name = "fechaDesde", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
            @RequestParam(name = "fechaHasta", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta,
            @RequestParam(name = "page", defaultValue = "0") int page,
            Model model,
            Authentication authentication) {

        // Para el sidebar / perfil
        addUsuarioToModel(model, authentication);

        int pageSize = 10;

        // El nombre de la propiedad es "id" (no "idVisitante")
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("id").descending());

        Page<Visitante> visitantesPage = visitanteRepository.buscarPorFiltros(
                nombre,
                cedula,
                fechaDesde,
                fechaHasta,
                pageable
        );

        int totalPages = visitantesPage.getTotalPages();
        List<Integer> pageNumbers = new ArrayList<>();
        for (int i = 0; i < totalPages; i++) {
            pageNumbers.add(i);
        }

        model.addAttribute("visitantesPage", visitantesPage);
        model.addAttribute("visitantes", visitantesPage.getContent());
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageNumbers", pageNumbers);
        model.addAttribute("currentPage", page);

        // Para mostrar arriba la fecha y hora actuales
        model.addAttribute("fechaHoy", LocalDate.now());
        model.addAttribute("horaAhora", LocalTime.now());

        // Para que se mantengan los filtros en el formulario
        model.addAttribute("filtroNombre", nombre);
        model.addAttribute("filtroCedula", cedula);
        model.addAttribute("filtroFechaDesde", fechaDesde);
        model.addAttribute("filtroFechaHasta", fechaHasta);

        return "vigilante/consultar-invitados";
    }



    // ============================================================
    //   GET: HISTORIAL DE REGISTROS (VISITANTES + REGISTROS MANUALES)
    // ============================================================
    @GetMapping("/historial-registro")
    public String historialRegistros(
            @RequestParam(name = "nombre", required = false) String nombre,
            @RequestParam(name = "cedula", required = false) String cedula,
            @RequestParam(name = "tipo", required = false) String tipo,
            @RequestParam(name = "fechaDesde", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
            @RequestParam(name = "fechaHasta", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta,
            @RequestParam(name = "page", defaultValue = "0") int page,
            Model model) {

        int pageSize = 10;
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("id").descending()); // ✅

        // 👉 Obtener TODOS los visitantes (paginados)
        Page<Visitante> visitantesPage = visitanteRepository.findAll(pageable);
        List<Visitante> visitantesList = new ArrayList<>(visitantesPage.getContent());

        // 👉 Obtener registros manuales (RegistroManual)
        List<RegistroManual> registrosManualList = registroManualRepository.findAllByOrderByFechaDescHoraDesc();
        if (registrosManualList == null) {
            registrosManualList = new ArrayList<>();
        }

        // 🔄 ENRIQUECER REGISTROS MANUALES CON DATOS DEL VISITANTE y AGRUPAR POR DÍA
        Map<String, RegistroManualDTO> registrosManualAgrupados = new LinkedHashMap<>();
        for (RegistroManual r : registrosManualList) {
            String nombreVisitante = "Desconocido";
            String apellidoVisitante = "-";

            if (r.getDocumento() != null && !r.getDocumento().trim().isEmpty()) {
                String docNormalizado = r.getDocumento().trim();

                // Primero buscar en visitantes con búsqueda flexible
                Optional<Visitante> visitanteOpt = visitanteRepository.findByCedulaFlexible(docNormalizado);
                if (visitanteOpt.isPresent()) {
                    nombreVisitante = visitanteOpt.get().getNombre();
                    apellidoVisitante = visitanteOpt.get().getApellido();
                } else {
                    // Si no encuentra en visitantes, buscar en usuarios con búsqueda flexible
                    Optional<Usuario> usuarioOpt = usuarioRepository.findByCedulaFlexible(docNormalizado);
                    if (usuarioOpt.isPresent()) {
                        nombreVisitante = usuarioOpt.get().getNombre();
                        apellidoVisitante = usuarioOpt.get().getApellido();
                    }
                }
            }

            // Crear clave de agrupación: documento + fecha
            String clave = r.getDocumento() + "_" + r.getFecha();

            if (registrosManualAgrupados.containsKey(clave)) {
                // Si ya existe, combinar la entrada/salida
                RegistroManualDTO dto = registrosManualAgrupados.get(clave);
                if (r.getTipoMovimiento().equals("Ingreso")) {
                    dto.setHoraEntrada(r.getHora());
                    dto.setTipoMovimiento("Completo");
                } else if (r.getTipoMovimiento().equals("Salida")) {
                    dto.setHoraSalida(r.getHora());
                    dto.setTipoMovimiento("Completo");
                }
            } else {
                // Crear nuevo DTO
                RegistroManualDTO dto = new RegistroManualDTO(
                        r.getDocumento(),
                        nombreVisitante,
                        apellidoVisitante,
                        r.getMotivo(),
                        r.getFecha(),
                        r.getTipoMovimiento().equals("Ingreso") ? r.getHora() : null,
                        r.getTipoMovimiento().equals("Salida") ? r.getHora() : null
                );
                dto.setTipoMovimiento(r.getTipoMovimiento());
                registrosManualAgrupados.put(clave, dto);
            }
        }

        List<RegistroManualDTO> registrosManualEnriquecidos = new ArrayList<>(registrosManualAgrupados.values());

        // 🔍 APLICAR FILTROS
        // Filtrar visitantes
        List<Visitante> visitantesFiltrados = visitantesList.stream()
                .filter(v -> nombre == null || nombre.isEmpty() ||
                        v.getNombre().toLowerCase().contains(nombre.toLowerCase()) ||
                        v.getApellido().toLowerCase().contains(nombre.toLowerCase()))
                .filter(v -> cedula == null || cedula.isEmpty() ||
                        (v.getCedula() != null && v.getCedula().contains(cedula)))
                .filter(v -> tipo == null || tipo.isEmpty() || "Visitante".equalsIgnoreCase(tipo))
                .filter(v -> {
                    if (v.getAsistenciaSede() == null) return true;
                    LocalDate fecha = v.getAsistenciaSede().getFecha();
                    if (fechaDesde != null && fecha.isBefore(fechaDesde)) return false;
                    return fechaHasta == null || !fecha.isAfter(fechaHasta);
                })
                .toList();

        // Filtrar registros manuales
        List<RegistroManualDTO> registrosFiltrados = registrosManualEnriquecidos.stream()
                .filter(r -> nombre == null || nombre.isEmpty() ||
                        r.getNombre().toLowerCase().contains(nombre.toLowerCase()) ||
                        r.getApellido().toLowerCase().contains(nombre.toLowerCase()))
                .filter(r -> cedula == null || cedula.isEmpty() || r.getDocumento().contains(cedula))
                .filter(r -> tipo == null || tipo.isEmpty() || "Manual".equalsIgnoreCase(tipo))
                .filter(r -> {
                    if (fechaDesde != null && r.getFecha().isBefore(fechaDesde)) return false;
                    return fechaHasta == null || !r.getFecha().isAfter(fechaHasta);
                })
                .toList();

        // Combinar (por si lo usas en la vista)
        List<Object> registrosCombinados = new ArrayList<>();
        registrosCombinados.addAll(visitantesFiltrados);
        registrosCombinados.addAll(registrosFiltrados);

        int totalPages = visitantesPage.getTotalPages();
        List<Integer> pageNumbers = new ArrayList<>();
        for (int i = 0; i < totalPages; i++) {
            pageNumbers.add(i);
        }

        model.addAttribute("visitantes", visitantesFiltrados);
        model.addAttribute("registrosManual", registrosFiltrados);
        model.addAttribute("registrosCombinados", registrosCombinados);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageNumbers", pageNumbers);
        model.addAttribute("currentPage", page);

        // Parámetros para que se mantengan en el formulario
        model.addAttribute("filtroNombre", nombre);
        model.addAttribute("filtroCedula", cedula);
        model.addAttribute("filtroTipo", tipo);
        model.addAttribute("filtroFechaDesde", fechaDesde);
        model.addAttribute("filtroFechaHasta", fechaHasta);

        // Para mostrar arriba la fecha y hora actuales
        model.addAttribute("fechaHoy", LocalDate.now());
        model.addAttribute("horaAhora", LocalTime.now());

        return "vigilante/historial-registro";
    }

    // ============================================================
    //   GET: PERFIL DEL VIGILANTE
    // ============================================================
    @GetMapping("/perfil")
    public String mostrarPerfil(Model model, Authentication authentication) {
        String cedula = authentication.getName();
        Optional<Usuario> usuarioOpt = usuarioRepository.findByCedula(cedula);

        if (usuarioOpt.isPresent()) {
            model.addAttribute("usuario", usuarioOpt.get());
            return "vigilante/perfil";
        }

        return "redirect:/login";
    }

    // ============================================================
    //   POST: ACTUALIZAR FOTO DE PERFIL DEL VIGILANTE
    // ============================================================
    @PostMapping("/actualizar-foto-perfil")
    public String actualizarFotoPerfil(
            @RequestParam("foto_perfil") MultipartFile file,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        try {
            String cedula = authentication.getName();
            Optional<Usuario> usuarioOpt = usuarioRepository.findByCedula(cedula);

            if (usuarioOpt.isEmpty()) {
                redirectAttributes.addAttribute("error", "Usuario no encontrado");
                return "redirect:/vigilante/perfil?error=true";
            }

            Usuario usuario = usuarioOpt.get();

            if (file.isEmpty()) {
                redirectAttributes.addAttribute("error", "Por favor seleccione una imagen");
                return "redirect:/vigilante/perfil?error=true";
            }

            // Validar tipo de archivo
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                redirectAttributes.addAttribute("error", "El archivo debe ser una imagen");
                return "redirect:/vigilante/perfil?error=true";
            }

            // Crear carpeta de uploads si no existe
            String uploadDir = "uploads";
            java.nio.file.Path uploadPath = java.nio.file.Paths.get(uploadDir);
            if (!java.nio.file.Files.exists(uploadPath)) {
                java.nio.file.Files.createDirectories(uploadPath);
            }

            // Generar nombre único para la foto
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || !originalFilename.contains(".")) {
                throw new IllegalArgumentException("Archivo no válido");
            }
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            long timestamp = System.currentTimeMillis();
            String newFilename = "perfil_" + usuario.getIdUsuario() + "_" + timestamp + fileExtension;

            // Guardar el archivo
            java.nio.file.Path filePath = uploadPath.resolve(newFilename);
            file.transferTo(filePath.toFile());

            redirectAttributes.addAttribute("success", "true");
            return "redirect:/vigilante/perfil?success=true";

        } catch (Exception e) {
            redirectAttributes.addAttribute("error", "Error al subir la foto");
            return "redirect:/vigilante/perfil?error=true";
        }
    }
}
