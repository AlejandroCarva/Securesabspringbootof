package com.tallerjava.taller.controller;

import com.tallerjava.taller.model.Notification;
import com.tallerjava.taller.model.Usuario;
import com.tallerjava.taller.repository.UsuarioRepository;
import com.tallerjava.taller.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificationController {

    private final NotificationService service;
    private final UsuarioRepository usuarioRepository;
    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);

    public NotificationController(NotificationService service, UsuarioRepository usuarioRepository) {
        this.service = service;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/unread-count")
    public Map<String, Object> unreadCount(@RequestParam(value = "userId", required = false) Integer userId) {
        long c = service.countUnread(userId);
        Map<String, Object> resp = new HashMap<>();
        resp.put("unreadCount", c);
        return resp;
    }

    @GetMapping("")
    public ResponseEntity<List<Notification>> list(@RequestParam(value = "userId", required = false) Integer userId,
                                                   @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit) {
        return ResponseEntity.ok(service.listLatest(userId, limit));
    }

    @PostMapping("/mark-read")
    public ResponseEntity<Map<String,Object>> markRead(@RequestBody Map<String,Object> body) {
        Object idObj = body.get("id");
        if (idObj == null) return ResponseEntity.badRequest().body(Map.of("success", false, "error", "id requerido"));
        Long id = null;
        try { id = Long.parseLong(String.valueOf(idObj)); } catch(Exception e) { return ResponseEntity.badRequest().body(Map.of("success", false, "error", "id inválido")); }
        var n = service.markRead(id);
        if (n == null) return ResponseEntity.status(404).body(Map.of("success", false, "error", "not found"));
        return ResponseEntity.ok(Map.of("success", true));
    }

    @PostMapping("/mark-all-read")
    public ResponseEntity<Map<String,Object>> markAllRead(@RequestParam(value = "userId", required = false) Integer userId) {
        service.markAllRead(userId);
        return ResponseEntity.ok(Map.of("success", true));
    }

    // Helper to create a notification (can be used by code that, e.g., receives instructor response and creates a notification for coordinator)
    @PostMapping("/create")
    public ResponseEntity<Map<String,Object>> create(@RequestBody Map<String,Object> body) {
        try {
            String titulo = (String) body.getOrDefault("titulo", "Notificación");
            String mensaje = (String) body.getOrDefault("mensaje", "");
            Integer usuarioId = body.get("usuarioId") != null ? Integer.parseInt(body.get("usuarioId").toString()) : null;
            Integer novedadId = body.get("novedadId") != null ? Integer.parseInt(body.get("novedadId").toString()) : null;
            String link = body.get("link") != null ? body.get("link").toString() : null;

            Notification n = new Notification();
            n.setTitulo(titulo);
            n.setMensaje(mensaje);
            n.setNovedadId(novedadId);
            n.setLink(link);
            if (usuarioId != null) {
                Usuario u = usuarioRepository.findById(usuarioId).orElse(null);
                n.setUsuario(u);
            }

            var saved = service.save(n);
            return ResponseEntity.ok(Map.of("success", true, "id", saved.getId()));
        } catch(Exception ex) {
            logger.error("create notification error", ex);
            return ResponseEntity.internalServerError().body(Map.of("success", false));
        }
    }
}
