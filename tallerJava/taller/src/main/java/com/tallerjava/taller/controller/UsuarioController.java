package com.tallerjava.taller.controller;

import com.tallerjava.taller.repository.UsuarioRepository;
import com.tallerjava.taller.model.Usuario;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/coordinador/usuario")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;

    public UsuarioController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @SuppressWarnings("null")
    @GetMapping("/me")
    public Map<String, Object> me() {
        Map<String, Object> resp = new HashMap<>();
        try {
            // Primero, si en la sesión hay un id de usuario (modo desarrollo/impersonate), usamos ese
            Usuario u = null;
            try {
                HttpSession session = null;
                try { session = ((HttpServletRequest) org.springframework.web.context.request.RequestContextHolder.currentRequestAttributes().resolveReference(org.springframework.web.context.request.RequestAttributes.REFERENCE_REQUEST)).getSession(false); } catch (Exception e) { /* ignore */ }
                if (session != null) {
                    Object sid = session.getAttribute("currentUserId");
                    if (sid instanceof Integer) {
                        u = usuarioRepository.findById((Integer) sid).orElse(null);
                    } else if (sid instanceof String) {
                        try { u = usuarioRepository.findById(Integer.valueOf((String) sid)).orElse(null); } catch (Exception ex) { }
                    }
                }

            } catch (Exception e) {
                // ignore
            }

            // Intentar obtener el usuario autenticado via Spring Security si aún no hay usuario
            if (u == null) {
                try {
                    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                    if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
                        String nombreUsuario = auth.getName();
                        // Intentar buscar por correo (si el nombre de usuario es el email)
                        // u = usuarioRepository.findByCorreo(nombreUsuario).orElse(null);
                        // Si no lo encontramos por correo, usar el primer usuario como fallback más abajo
                    }
                } catch (NoClassDefFoundError | Exception e) {
                    // Spring Security no configurado o error; seguiremos con fallback
                }
            }

            // Si no hay usuario autenticado o no se encontró, usar el primer registro con fetch joins
            if (u == null) {
                // u = usuarioRepository.findFirstWithRolesAndFicha().orElse(null);
                u = usuarioRepository.findAll().stream().findFirst().orElse(null);
            }
            if (u == null) {
                resp.put("nombre", "Invitado");
                resp.put("email", "");
                resp.put("rol", "Invitado");
                resp.put("ficha", "");
                return resp;
            }
            resp.put("nombre", u.getNombre() != null ? u.getNombre() : "");
            resp.put("apellido", u.getApellido() != null ? u.getApellido() : "");
            resp.put("nombre", u.getNombre() != null ? u.getNombre() : "");
            resp.put("email", u.getCorreo() != null ? u.getCorreo() : "");
            // obtener primer rol si existe
            String rolNombre = "Aprendiz";
            if (u.getRoles() != null && !u.getRoles().isEmpty()) {
                try { rolNombre = u.getRoles().get(0).getName(); } catch (Exception ex) { /* ignore */ }
            }
            resp.put("rol", rolNombre);
            resp.put("ficha", u.getFicha() != null ? u.getFicha().getNumeroFicha() : "");
        } catch (Exception e) {
            resp.put("nombre", "Invitado");
            resp.put("email", "");
            resp.put("rol", "Invitado");
            resp.put("ficha", "");
        }
        return resp;
    }

    @GetMapping("/coordinador")
    public Map<String, Object> getCoordinador() {
        Map<String, Object> resp = new HashMap<>();
        try {
            // java.util.List<Usuario> coords = usuarioRepository.findByRoleNameIgnoreCase("coordinador");
            // Usuario u = coords != null && !coords.isEmpty() ? coords.get(0) : null;
            Usuario u = null;
            // Fallback: si no hay rol con nombre 'coordinador', intentar por roleId (ej. 5)
            if (u == null) {
                try {
                    // java.util.List<Usuario> coordsById = usuarioRepository.findByRoleId(5);
                    // if (coordsById != null && !coordsById.isEmpty()) u = coordsById.get(0);
                } catch (Exception ex) {
                    // ignore
                }
            }
            if (u == null) {
                resp.put("nombre", "Invitado");
                resp.put("email", "");
                resp.put("rol", "Invitado");
                resp.put("ficha", "");
                return resp;
            }
            resp.put("apellido", u.getApellido() != null ? u.getApellido() : "");
            resp.put("nombre", u.getNombre() != null ? u.getNombre() : "");
            resp.put("email", u.getCorreo() != null ? u.getCorreo() : "");
            String rolNombre = "Coordinador";
            if (u.getRoles() != null && !u.getRoles().isEmpty()) {
                try { rolNombre = u.getRoles().get(0).getName(); } catch (Exception ex) { /* ignore */ }
            }
            resp.put("rol", rolNombre);
            resp.put("ficha", u.getFicha() != null ? u.getFicha().getNumeroFicha() : "");
        } catch (Exception e) {
            resp.put("nombre", "Invitado");
            resp.put("email", "");
            resp.put("rol", "Invitado");
            resp.put("ficha", "");
        }
        return resp;
    }

    /**
     * Endpoint de desarrollo para 'impersonar' un usuario: guarda el id en la sesión.
     * Uso: POST /coordinador/usuario/impersonate?id=14
     */
    @PostMapping("/impersonate")
    public Map<String, Object> impersonate(@org.springframework.web.bind.annotation.RequestParam("id") Integer id, HttpServletRequest request) {
        Map<String, Object> r = new HashMap<>();
        try {
            if (id == null) {
                r.put("success", false);
                r.put("message", "id requerido");
                return r;
            }
            HttpSession session = request.getSession(true);
            session.setAttribute("currentUserId", id);
            r.put("success", true);
            r.put("id", id);
        } catch (Exception e) {
            r.put("success", false);
            r.put("message", e.getMessage());
        }
        return r;
    }
}
