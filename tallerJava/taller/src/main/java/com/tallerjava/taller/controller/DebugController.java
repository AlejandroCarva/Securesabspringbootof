package com.tallerjava.taller.controller;

import com.tallerjava.taller.model.Role;
import com.tallerjava.taller.model.Usuario;
import com.tallerjava.taller.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/debug")
public class DebugController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/check-user/{cedula}")
    public Map<String, Object> checkUser(@PathVariable String cedula, @RequestParam String password) {
        Map<String, Object> result = new HashMap<>();

        Usuario u = usuarioRepository.findByCedula(cedula).orElse(null);

        if (u == null) {
            result.put("found", false);
            result.put("message", "Usuario no encontrado");
            return result;
        }

        result.put("found", true);
        result.put("cedula", u.getCedula());
        result.put("correo", u.getCorreo());
        result.put("nombre", u.getNombre() + " " + u.getApellido());

        String storedPassword = u.getPassword();
        result.put("passwordPresent", storedPassword != null && !storedPassword.isEmpty());
        result.put("passwordFormat", detectPasswordFormat(storedPassword));

        // Test password match
        boolean matches = passwordEncoder.matches(password, storedPassword);
        result.put("passwordMatches", matches);

        // Roles
        if (u.getRoles() != null && !u.getRoles().isEmpty()) {
            result.put("roles", u.getRoles().stream()
                    .map(Role::getName)
                    .collect(Collectors.toList()));
        } else {
            result.put("roles", "NO ROLES");
        }

        return result;
    }

    @GetMapping("/list-users")
    public Map<String, Object> listUsers() {
        Map<String, Object> result = new HashMap<>();
        var usuarios = usuarioRepository.findAll();

        result.put("totalUsers", usuarios.size());
        result.put("usuarios", usuarios.stream()
                .map(u -> {
                    Map<String, Object> userData = new HashMap<>();
                    userData.put("cedula", u.getCedula());
                    userData.put("correo", u.getCorreo());
                    userData.put("nombre", u.getNombre() + " " + u.getApellido());
                    userData.put("roles", u.getRoles() != null ?
                            u.getRoles().stream().map(Role::getName).collect(Collectors.toList()) :
                            "NO ROLES");
                    return userData;
                })
                .collect(Collectors.toList()));

        return result;
    }

    @PostMapping("/test-login")
    public Map<String, Object> testLogin(@RequestParam String username, @RequestParam String password) {
        Map<String, Object> result = new HashMap<>();
        result.put("receivedUsername", username);
        result.put("receivedPassword", password != null ? "***" + password.substring(Math.max(0, password.length()-3)) : "null");
        result.put("postWorking", true);
        return result;
    }

    private String detectPasswordFormat(String password) {
        if (password == null || password.isEmpty()) return "EMPTY";
        if (password.startsWith("$2a$")) return "BCRYPT_2A";
        if (password.startsWith("$2b$")) return "BCRYPT_2B";
        if (password.startsWith("$2y$")) return "BCRYPT_2Y";
        if (password.startsWith("$y$")) return "PHP_Y";
        if (password.startsWith("$argon2")) return "ARGON2";
        return "PLAIN_TEXT";
    }
}
