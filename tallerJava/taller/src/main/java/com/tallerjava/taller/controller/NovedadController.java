package com.tallerjava.taller.controller;

import com.tallerjava.taller.model.Novedad;
import com.tallerjava.taller.model.Usuario;
import com.tallerjava.taller.model.Ficha;
import com.tallerjava.taller.repository.FichaRepository;
import com.tallerjava.taller.repository.NovedadRepository;
import com.tallerjava.taller.repository.UsuarioRepository;
import com.tallerjava.taller.service.NovedadService;
import com.tallerjava.taller.service.NovedadExportService;
import org.springframework.stereotype.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Map;
import java.util.HashMap;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.io.IOException;

@Controller
@RequestMapping("/coordinador/novedades")
public class NovedadController {

	private final NovedadService novedadService;
	private final FichaRepository fichaRepository;
	private final UsuarioRepository usuarioRepository;
	private final NovedadRepository novedadRepository;
	private final NovedadExportService exportService;

	public NovedadController(NovedadService novedadService, FichaRepository fichaRepository, 
							 UsuarioRepository usuarioRepository, NovedadRepository novedadRepository,
							 NovedadExportService exportService) {
		this.novedadService = novedadService;
		this.fichaRepository = fichaRepository;
		this.usuarioRepository = usuarioRepository;
		this.novedadRepository = novedadRepository;
		this.exportService = exportService;
	}

	// Devuelve información básica del instructor (JSON) para poblar el modal
	@GetMapping("/instructor/{id}")
	@ResponseBody
	public Map<String, String> obtenerInstructorJson(@PathVariable("id") Integer id) {
		Map<String, String> resp = new HashMap<>();
		if (id == null) return resp;
		Usuario u = usuarioRepository.findById(id).orElse(null);
		if (u == null) return resp;
		resp.put("id", u.getId() != null ? u.getId().toString() : "");
		resp.put("nombre", u.getNombre() != null ? u.getNombre() : "");
		resp.put("apellido", u.getApellido() != null ? u.getApellido() : "");
		resp.put("cedula", u.getCedula() != null ? u.getCedula() : "");
		resp.put("correo", u.getCorreo() != null ? u.getCorreo() : "");
		resp.put("telefono", u.getTelefono() != null ? u.getTelefono() : "");
		return resp;
	}

	// Devuelve detalles completos de una novedad en JSON (para modal de detalles)
	@GetMapping("/detalles/{id}")
	@ResponseBody
	public Map<String, Object> obtenerDetallesNovedad(@PathVariable("id") Integer id) {
		Map<String, Object> resp = new HashMap<>();
		if (id == null) return resp;
		Novedad nov = novedadRepository.findById(id).orElse(null);
		if (nov == null) return resp;
		resp.put("id", nov.getId());
		resp.put("titulo", nov.getTitulo() != null ? nov.getTitulo() : "");
		resp.put("descripcion", nov.getDescripcion() != null ? nov.getDescripcion() : "");
		resp.put("fecha", nov.getFecha() != null ? nov.getFecha().toString() : "");
		resp.put("estado", nov.getEstado() != null ? nov.getEstado() : "");
		resp.put("respuestaCoordinador", nov.getRespuesta() != null ? nov.getRespuesta() : "");
		if (nov.getFicha() != null) {
			resp.put("fichaId", nov.getFicha().getId());
			resp.put("fichaNumero", nov.getFicha().getNumeroFicha());
		} else {
			resp.put("fichaId", null);
			resp.put("fichaNumero", "");
		}
		if (nov.getInstructor() != null) {
			Usuario u = nov.getInstructor();
			Map<String, String> iu = new HashMap<>();
			iu.put("id", u.getId() != null ? u.getId().toString() : "");
			iu.put("nombre", u.getNombre() != null ? u.getNombre() : "");
			iu.put("apellido", u.getApellido() != null ? u.getApellido() : "");
			iu.put("cedula", u.getCedula() != null ? u.getCedula() : "");
			iu.put("correo", u.getCorreo() != null ? u.getCorreo() : "");
			iu.put("telefono", u.getTelefono() != null ? u.getTelefono() : "");
			resp.put("instructor", iu);
		} else {
			resp.put("instructor", null);
		}
		resp.put("respuestaInstructor", "");
		resp.put("archivoAdjunto", nov.getArchivoAdjunto() != null ? nov.getArchivoAdjunto() : "");
		return resp;
	}

	private static final Logger logger = LoggerFactory.getLogger(NovedadController.class);

	// Carpeta donde se guardarán los adjuntos (relativa al working dir)
	private final Path uploadsDir = Paths.get("uploads");

	private String saveUploadedFile(MultipartFile file) {
		if (file == null || file.isEmpty()) return null;
		try {
			if (!Files.exists(uploadsDir)) {
				Files.createDirectories(uploadsDir);
			}
			String original = Path.of(file.getOriginalFilename() == null ? "file" : file.getOriginalFilename()).getFileName().toString();
			String filename = System.currentTimeMillis() + "_" + original.replaceAll("\\\s+","_");
			Path target = uploadsDir.resolve(filename).normalize();
			Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
			return filename;
		} catch (IOException e) {
			logger.error("Error guardando archivo adjunto", e);
			return null;
		}
	}

	@GetMapping("/archivo/{filename:.+}")
	public ResponseEntity<Resource> descargarArchivo(@PathVariable("filename") String filename) {
		try {
			Path filePath = uploadsDir.resolve(filename).normalize();
			Resource resource = new UrlResource(filePath.toUri());
			if (!resource.exists()) {
				return ResponseEntity.notFound().build();
			}
			String contentDisposition = "attachment; filename=\"" + resource.getFilename() + "\"";
			return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
				.contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(resource);
		} catch (Exception ex) {
			logger.error("Error al leer archivo: {}", filename, ex);
			return ResponseEntity.status(500).build();
		}
	}

	// Endpoint para obtener todas las novedades en formato JSON (para el dashboard)
	@GetMapping("/listar-json")
	@ResponseBody
	public List<Map<String, Object>> listarNovedadesJson() {
		List<Novedad> novedades = novedadRepository.findAll();
		return novedades.stream().map(nov -> {
			Map<String, Object> data = new HashMap<>();
			data.put("id", nov.getId());
			data.put("titulo", nov.getTitulo() != null ? nov.getTitulo() : "");
			data.put("descripcion", nov.getDescripcion() != null ? nov.getDescripcion() : "");
			data.put("tipo", nov.getTipo() != null ? nov.getTipo() : "");
			data.put("estado", nov.getEstado() != null ? nov.getEstado() : "Pendiente");
			data.put("fecha", nov.getFecha() != null ? nov.getFecha().toString() : "");
			data.put("respuesta", nov.getRespuesta() != null ? nov.getRespuesta() : "");
			if (nov.getFicha() != null) {
				data.put("fichaNumero", nov.getFicha().getNumeroFicha());
			}
			if (nov.getInstructor() != null) {
				data.put("instructorNombre", nov.getInstructor().getNombre() + " " + nov.getInstructor().getApellido());
			}
			return data;
		}).collect(Collectors.toList());
	}

	@GetMapping("/crear")
	public String mostrarFormularioCrear(Model model,
			@org.springframework.web.bind.annotation.RequestParam(value = "fichaId", required = false) Integer fichaId,
			@org.springframework.web.bind.annotation.RequestParam(value = "instructorId", required = false) Integer instructorId,
			@org.springframework.web.bind.annotation.RequestParam(value = "estado", required = false) String estado) {
		model.addAttribute("novedad", new Novedad());
		java.util.List<com.tallerjava.taller.model.Novedad> all = novedadService.listar();
		java.util.stream.Stream<com.tallerjava.taller.model.Novedad> stream = all.stream();
		if (fichaId != null) stream = stream.filter(n -> n.getFicha() != null && fichaId.equals(n.getFicha().getId()));
		if (instructorId != null) stream = stream.filter(n -> n.getInstructor() != null && instructorId.equals(n.getInstructor().getId()));
		if (estado != null && !estado.isBlank()) stream = stream.filter(n -> n.getEstado() != null && n.getEstado().equalsIgnoreCase(estado));
		java.util.List<com.tallerjava.taller.model.Novedad> filtered = stream.toList();
		model.addAttribute("listaNovedades", filtered);
		model.addAttribute("listaFichas", fichaRepository.findAll());
		// Mostrar sólo usuarios con rol 'Instructor' (insensible a mayúsculas)
		model.addAttribute("listaInstructores", usuarioRepository.findByRoleNameIgnoreCase("instructor"));
		return "coordinador/crear-novedad";
	}

	@PostMapping("/cambiar-estado")
	public String cambiarEstado(@org.springframework.web.bind.annotation.RequestParam("id") Integer id,
			@org.springframework.web.bind.annotation.RequestParam("estado") String estado,
			RedirectAttributes redirectAttributes) {
		var n = novedadRepository.findById(id).orElse(null);
		if (n == null) {
			redirectAttributes.addFlashAttribute("errors", java.util.List.of("Novedad no encontrada"));
			return "redirect:/coordinador/novedades/crear";
		}
		n.setEstado(estado);
		novedadRepository.save(n);
		redirectAttributes.addFlashAttribute("success", "Estado actualizado.");
		return "redirect:/coordinador/novedades/crear";
	}

	@GetMapping("/crear-debug")
	public String mostrarFormularioDebug(Model model) {
		model.addAttribute("listaNovedades", novedadService.listar());
		model.addAttribute("listaFichas", new java.util.ArrayList<>());
		model.addAttribute("listaInstructores", new java.util.ArrayList<>());
		return "coordinador/crear-novedad-debug";
	}

	@GetMapping("/debug-fichas")
	@org.springframework.web.bind.annotation.ResponseBody
	public String debugFichas() {
		StringBuilder sb = new StringBuilder();
		java.util.List<com.tallerjava.taller.model.Ficha> fichas = fichaRepository.findAll();
		sb.append("Fichas encontradas: ").append(fichas.size()).append("\n");
		for (com.tallerjava.taller.model.Ficha f : fichas) {
			sb.append("id=").append(f.getId()).append(" numeroFicha=").append(f.getNumeroFicha()).append("\n");
		}
		return sb.toString();
	}

	@GetMapping("/debug-usuarios")
	@org.springframework.web.bind.annotation.ResponseBody
	public String debugUsuarios() {
		StringBuilder sb = new StringBuilder();
		java.util.List<com.tallerjava.taller.model.Usuario> usuarios = usuarioRepository.findAll();
		sb.append("Usuarios encontradas: ").append(usuarios.size()).append("\n");
		for (com.tallerjava.taller.model.Usuario u : usuarios) {
			sb.append("id=").append(u.getId()).append(" nombre=").append(u.getNombre()).append(" ").append(u.getApellido()).append("\n");
		}
		return sb.toString();
	}

	@GetMapping("/debug-novedades")
	@org.springframework.web.bind.annotation.ResponseBody
	public String debugNovedades() {
		StringBuilder sb = new StringBuilder();
		java.util.List<com.tallerjava.taller.model.Novedad> novedades = novedadRepository.findAll();
		sb.append("Novedades encontradas: ").append(novedades.size()).append("\n");
		for (com.tallerjava.taller.model.Novedad n : novedades) {
			sb.append("id=").append(n.getId()).append(" titulo=").append(n.getTitulo()).append(" fecha=").append(n.getFecha()).append("\n");
		}
		return sb.toString();
	}

	@PostMapping("/eliminar")
	public String eliminarNovedad(@org.springframework.web.bind.annotation.RequestParam("id") Integer id,
								  RedirectAttributes redirectAttributes) {
		if (id == null) {
			redirectAttributes.addFlashAttribute("errors", java.util.List.of("Id no proporcionado."));
			return "redirect:/coordinador/novedades/crear";
		}
		var n = novedadRepository.findById(id).orElse(null);
		if (n == null) {
			redirectAttributes.addFlashAttribute("errors", java.util.List.of("Novedad no encontrada."));
			return "redirect:/coordinador/novedades/crear";
		}
		try {
			novedadRepository.delete(n);
			redirectAttributes.addFlashAttribute("success", "Novedad eliminada correctamente.");
		} catch (Exception ex) {
			redirectAttributes.addFlashAttribute("errors", java.util.List.of("No se pudo eliminar la novedad."));
		}
		return "redirect:/coordinador/novedades/crear";
	}

	@PostMapping("/guardar")
	public String guardarNovedad(@ModelAttribute("novedad") Novedad novedad,
			@org.springframework.web.bind.annotation.RequestParam(value = "instructorId", required = false) Integer instructorId,
			@org.springframework.web.bind.annotation.RequestParam(value = "fichaId", required = false) Integer fichaId,
			@org.springframework.web.bind.annotation.RequestParam(value = "archivo", required = false) MultipartFile archivo,
			Model model,
			RedirectAttributes redirectAttributes) {

		logger.info("guardarNovedad called - crear novedad , titulo='{}', fichaId={}, instructorId={}", novedad.getTitulo(), fichaId, instructorId);

		List<String> errors = new ArrayList<>();
		if (fichaId == null) {
			errors.add("Seleccione la ficha afectada.");
		}
		if (instructorId == null) {
			errors.add("Seleccione el instructor responsable.");
		}
		if (novedad.getTitulo() == null || novedad.getTitulo().isBlank()) {
			errors.add("El título es obligatorio.");
		}
		if (novedad.getDescripcion() == null || novedad.getDescripcion().isBlank()) {
			errors.add("La descripción es obligatoria.");
		}

		if (!errors.isEmpty()) {
			model.addAttribute("errors", errors);
			model.addAttribute("listaFichas", fichaRepository.findAll());
			model.addAttribute("listaInstructores", usuarioRepository.findByRoleNameIgnoreCase("instructor"));
			model.addAttribute("listaNovedades", novedadService.listar());
			return "coordinador/crear-novedad";
		}

		Usuario instructor = null;
		Ficha ficha = null;
		if (instructorId != null) {
			instructor = usuarioRepository.findById(instructorId).orElse(null);
		}
		if (fichaId != null) {
			ficha = fichaRepository.findById(fichaId).orElse(null);
		}

		novedad.setInstructor(instructor);
		novedad.setFicha(ficha);
		if (novedad.getCoordinador() == null) {
			java.util.List<Usuario> usuarios = usuarioRepository.findAll();
			if (!usuarios.isEmpty()) novedad.setCoordinador(usuarios.get(0));
		}
		if (novedad.getFecha() == null) novedad.setFecha(java.time.LocalDate.now());
		if (novedad.getEstado() == null) novedad.setEstado("Pendiente");

		// Guardar archivo adjunto si se envió
		if (archivo != null && !archivo.isEmpty()) {
			String saved = saveUploadedFile(archivo);
			if (saved != null) {
				novedad.setArchivoAdjunto(saved);
			}
		}

		novedadService.guardar(novedad);
		logger.info("Novedad creada - titulo='{}'", novedad.getTitulo());
		redirectAttributes.addFlashAttribute("success", "Novedad registrada correctamente.");
		return "redirect:/coordinador/novedades/crear";
	}

	@PostMapping("/guardar-ajax")
	@ResponseBody
	public Map<String, Object> guardarNovedadAjax(
			@org.springframework.web.bind.annotation.RequestParam(value = "titulo", required = false) String titulo,
			@org.springframework.web.bind.annotation.RequestParam(value = "descripcion", required = false) String descripcion,
			@org.springframework.web.bind.annotation.RequestParam(value = "instructorId", required = false) Integer instructorId,
			@org.springframework.web.bind.annotation.RequestParam(value = "fichaId", required = false) Integer fichaId,
			@org.springframework.web.bind.annotation.RequestParam(value = "archivo", required = false) MultipartFile archivo
	) {
		Map<String, Object> resp = new HashMap<>();
		List<String> errors = new ArrayList<>();
		if (fichaId == null) errors.add("Seleccione la ficha afectada.");
		if (instructorId == null) errors.add("Seleccione el instructor responsable.");
		if (titulo == null || titulo.isBlank()) errors.add("El título es obligatorio.");
		if (descripcion == null || descripcion.isBlank()) errors.add("La descripción es obligatoria.");

		if (!errors.isEmpty()) {
			resp.put("success", false);
			resp.put("errors", errors);
			return resp;
		}

		Usuario instructor = null;
		Ficha ficha = null;
		if (instructorId != null) {
			instructor = usuarioRepository.findById(instructorId).orElse(null);
		}
		if (fichaId != null) {
			ficha = fichaRepository.findById(fichaId).orElse(null);
		}

		Novedad nov = new Novedad();
		nov.setTitulo(titulo);
		nov.setDescripcion(descripcion);
		nov.setInstructor(instructor);
		nov.setFicha(ficha);
		java.util.List<Usuario> usuarios = usuarioRepository.findAll();
		if (!usuarios.isEmpty()) nov.setCoordinador(usuarios.get(0));
		nov.setFecha(java.time.LocalDate.now());
		nov.setEstado("Pendiente");

		// Guardar archivo adjunto si se envió
		if (archivo != null && !archivo.isEmpty()) {
			String saved = saveUploadedFile(archivo);
			if (saved != null) {
				nov.setArchivoAdjunto(saved);
			}
		}

		novedadService.guardar(nov);

		Map<String, Object> data = new HashMap<>();
		data.put("id", nov.getId());
		data.put("titulo", nov.getTitulo());
		data.put("descripcion", nov.getDescripcion());
		data.put("fecha", nov.getFecha() != null ? nov.getFecha().toString() : "");
		data.put("estado", nov.getEstado());
		data.put("respuesta", nov.getRespuesta() != null ? nov.getRespuesta() : "");
		if (nov.getInstructor() != null) {
			Map<String, String> iu = new HashMap<>();
			iu.put("id", nov.getInstructor().getId() != null ? nov.getInstructor().getId().toString() : "");
			iu.put("nombre", nov.getInstructor().getNombre() != null ? nov.getInstructor().getNombre() : "");
			iu.put("apellido", nov.getInstructor().getApellido() != null ? nov.getInstructor().getApellido() : "");
			data.put("instructor", iu);
		}
		if (nov.getFicha() != null) {
			data.put("fichaId", nov.getFicha().getId());
			data.put("fichaNumero", nov.getFicha().getNumeroFicha());
		}
		data.put("archivoAdjunto", nov.getArchivoAdjunto() != null ? nov.getArchivoAdjunto() : "");

		resp.put("success", true);
		resp.put("novedad", data);
		return resp;
	}

	@PostMapping("/actualizar")
	public String actualizarNovedad(@ModelAttribute("novedad") Novedad novedad,
			@org.springframework.web.bind.annotation.RequestParam(value = "id", required = false) Integer idParam,
			@org.springframework.web.bind.annotation.RequestParam(value = "instructorId", required = false) Integer instructorId,
			@org.springframework.web.bind.annotation.RequestParam(value = "fichaId", required = false) Integer fichaId,
			@org.springframework.web.bind.annotation.RequestParam(value = "archivo", required = false) MultipartFile archivo,
			Model model,
			RedirectAttributes redirectAttributes) {

		if (novedad.getId() == null && idParam != null) {
			try {
				novedad.setId(idParam);
			} catch (Exception ex) {
				// ignore and continue; validation below will catch null id
			}
		}

		logger.info("actualizarNovedad called - id={}, titulo='{}', fichaId={}, instructorId={}", novedad.getId(), novedad.getTitulo(), fichaId, instructorId);

		List<String> errors = new ArrayList<>();
		if (novedad.getId() == null) {
			errors.add("Id de la novedad no proporcionado para actualización.");
		}
		if (fichaId == null) {
			errors.add("Seleccione la ficha afectada.");
		}
		if (instructorId == null) {
			errors.add("Seleccione el instructor responsable.");
		}
		if (novedad.getTitulo() == null || novedad.getTitulo().isBlank()) {
			errors.add("El título es obligatorio.");
		}
		if (novedad.getDescripcion() == null || novedad.getDescripcion().isBlank()) {
			errors.add("La descripción es obligatoria.");
		}

		if (!errors.isEmpty()) {
			model.addAttribute("errors", errors);
			model.addAttribute("listaFichas", fichaRepository.findAll());
			model.addAttribute("listaInstructores", usuarioRepository.findByRoleNameIgnoreCase("instructor"));
			model.addAttribute("novedad", novedadRepository.findById(novedad.getId()).orElse(novedad));
			return "coordinador/editar-novedad";
		}

		Usuario instructor = null;
		Ficha ficha = null;
		if (instructorId != null) {
			instructor = usuarioRepository.findById(instructorId).orElse(null);
		}
		if (fichaId != null) {
			ficha = fichaRepository.findById(fichaId).orElse(null);
		}

		com.tallerjava.taller.model.Novedad toSave = novedadRepository.findById(novedad.getId()).orElse(null);
		if (toSave == null) {
			redirectAttributes.addFlashAttribute("errors", java.util.List.of("No se encontró la novedad a actualizar."));
			return "redirect:/coordinador/novedades/crear";
		}
		toSave.setTitulo(novedad.getTitulo());
		toSave.setDescripcion(novedad.getDescripcion());
		if (instructor != null) toSave.setInstructor(instructor);
		if (ficha != null) toSave.setFicha(ficha);
		if (novedad.getFecha() != null) toSave.setFecha(novedad.getFecha());
		if (novedad.getEstado() != null) toSave.setEstado(novedad.getEstado());
		if (novedad.getRespuesta() != null) toSave.setRespuesta(novedad.getRespuesta());

		// Si hay un archivo nuevo, guardarlo y actualizar referencia
		if (archivo != null && !archivo.isEmpty()) {
			String saved = saveUploadedFile(archivo);
			if (saved != null) {
				toSave.setArchivoAdjunto(saved);
			}
		}

		novedadService.guardar(toSave);
		logger.info("Novedad actualizada - id={}, titulo='{}'", toSave.getId(), toSave.getTitulo());
		redirectAttributes.addFlashAttribute("success", "Novedad actualizada correctamente.");
		return "redirect:/coordinador/novedades/crear";
	}

	@GetMapping("/editar-novedad/{id}")
	public String editarNovedad(@org.springframework.web.bind.annotation.PathVariable("id") Integer id, Model model) {
		Novedad nov = novedadRepository.findById(id).orElse(null);
		if (nov == null) {
			return "redirect:/coordinador/novedades/crear";
		}
		model.addAttribute("novedad", nov);
		model.addAttribute("listaFichas", fichaRepository.findAll());
		model.addAttribute("listaInstructores", usuarioRepository.findByRoleNameIgnoreCase("instructor"));
		return "coordinador/editar-novedad";
	}

	/**
	 * Filtra las novedades según los criterios proporcionados
	 */
	private List<Novedad> filtrarNovedades(String busqueda, String estado, String tipo, 
											String instructor, String ficha) {
		List<Novedad> novedades = novedadRepository.findAll();
		
		return novedades.stream()
			.filter(n -> {
				// Filtro de búsqueda (título, descripción)
				if (busqueda != null && !busqueda.trim().isEmpty()) {
					String searchLower = busqueda.toLowerCase();
					boolean matchTitulo = n.getTitulo() != null && n.getTitulo().toLowerCase().contains(searchLower);
					boolean matchDesc = n.getDescripcion() != null && n.getDescripcion().toLowerCase().contains(searchLower);
					if (!matchTitulo && !matchDesc) return false;
				}
				
				// Filtro de estado
				if (estado != null && !estado.trim().isEmpty()) {
					if (n.getEstado() == null || !n.getEstado().equalsIgnoreCase(estado)) {
						return false;
					}
				}
				
				// Filtro de tipo
				if (tipo != null && !tipo.trim().isEmpty()) {
					if (n.getTipo() == null || !n.getTipo().equalsIgnoreCase(tipo)) {
						return false;
					}
				}
				
				// Filtro de instructor
				if (instructor != null && !instructor.trim().isEmpty()) {
					if (n.getInstructor() == null) return false;
					String nombreCompleto = (n.getInstructor().getNombre() != null ? n.getInstructor().getNombre() : "") + 
										   " " + (n.getInstructor().getApellido() != null ? n.getInstructor().getApellido() : "");
					nombreCompleto = nombreCompleto.trim();
					if (!nombreCompleto.toLowerCase().contains(instructor.toLowerCase())) {
						return false;
					}
				}
				
				// Filtro de ficha
				if (ficha != null && !ficha.trim().isEmpty()) {
					if (n.getFicha() == null || n.getFicha().getNumeroFicha() == null) return false;
					if (!n.getFicha().getNumeroFicha().contains(ficha)) {
						return false;
					}
				}
				
				return true;
			})
			.collect(Collectors.toList());
	}

	/**
	 * Exportar novedades a Excel con filtros multicriterio
	 */
	@GetMapping("/exportar/excel")
	public ResponseEntity<byte[]> exportarExcel(
			@RequestParam(value = "busqueda", required = false) String busqueda,
			@RequestParam(value = "estado", required = false) String estado,
			@RequestParam(value = "tipo", required = false) String tipo,
			@RequestParam(value = "instructor", required = false) String instructor,
			@RequestParam(value = "ficha", required = false) String ficha) {
		
		try {
			logger.info("Exportando novedades a Excel con filtros: busqueda={}, estado={}, tipo={}, instructor={}, ficha={}", 
						busqueda, estado, tipo, instructor, ficha);
			
			List<Novedad> novedadesFiltradas = filtrarNovedades(busqueda, estado, tipo, instructor, ficha);
			byte[] excelBytes = exportService.exportarNovedadesExcel(novedadesFiltradas);
			
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			headers.setContentDispositionFormData("attachment", "novedades_reporte.xlsx");
			headers.setContentLength(excelBytes.length);
			
			return ResponseEntity.ok()
					.headers(headers)
					.body(excelBytes);
					
		} catch (Exception e) {
			logger.error("Error al exportar novedades a Excel", e);
			return ResponseEntity.internalServerError().build();
		}
	}

	/**
	 * Exportar novedades a PDF con filtros multicriterio
	 * Nota: Por ahora genera HTML imprimible como PDF
	 */
	@GetMapping("/exportar/pdf")
	public ResponseEntity<byte[]> exportarPDF(
			@RequestParam(value = "busqueda", required = false) String busqueda,
			@RequestParam(value = "estado", required = false) String estado,
			@RequestParam(value = "tipo", required = false) String tipo,
			@RequestParam(value = "instructor", required = false) String instructor,
			@RequestParam(value = "ficha", required = false) String ficha) {
		
		try {
			logger.info("Exportando novedades a PDF con filtros: busqueda={}, estado={}, tipo={}, instructor={}, ficha={}", 
						busqueda, estado, tipo, instructor, ficha);
			
			List<Novedad> novedadesFiltradas = filtrarNovedades(busqueda, estado, tipo, instructor, ficha);
			byte[] htmlBytes = exportService.exportarNovedadesPDF(novedadesFiltradas);
			
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.TEXT_HTML);
			headers.setContentLength(htmlBytes.length);
			
			return ResponseEntity.ok()
					.headers(headers)
					.body(htmlBytes);
					
		} catch (Exception e) {
			logger.error("Error al exportar novedades a PDF", e);
			return ResponseEntity.internalServerError().build();
		}
	}

	/**
	 * Exportar novedades a CSV con filtros multicriterio
	 */
	@GetMapping("/exportar/csv")
	public ResponseEntity<byte[]> exportarCSV(
			@RequestParam(value = "busqueda", required = false) String busqueda,
			@RequestParam(value = "estado", required = false) String estado,
			@RequestParam(value = "tipo", required = false) String tipo,
			@RequestParam(value = "instructor", required = false) String instructor,
			@RequestParam(value = "ficha", required = false) String ficha) {
		
		try {
			logger.info("Exportando novedades a CSV con filtros: busqueda={}, estado={}, tipo={}, instructor={}, ficha={}", 
						busqueda, estado, tipo, instructor, ficha);
			
			List<Novedad> novedadesFiltradas = filtrarNovedades(busqueda, estado, tipo, instructor, ficha);
			byte[] csvBytes = exportService.exportarNovedadesCSV(novedadesFiltradas);
			
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.parseMediaType("text/csv"));
			headers.setContentDispositionFormData("attachment", "novedades_reporte.csv");
			headers.setContentLength(csvBytes.length);
			
			return ResponseEntity.ok()
					.headers(headers)
					.body(csvBytes);
					
		} catch (Exception e) {
			logger.error("Error al exportar novedades a CSV", e);
			return ResponseEntity.internalServerError().build();
		}
	}

}
