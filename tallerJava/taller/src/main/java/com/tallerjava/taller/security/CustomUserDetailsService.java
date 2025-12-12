package com.tallerjava.taller.security;

import com.tallerjava.taller.model.Usuario;
import com.tallerjava.taller.model.Role;
import com.tallerjava.taller.repository.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String cedula) throws UsernameNotFoundException {
        System.out.println("? CUSTOM USER DETAILS - Buscando usuario con cédula: " + cedula);

        Usuario usuario = usuarioRepository.findByCedula(cedula)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + cedula));

        System.out.println("? Usuario encontrado: " + usuario.getNombre() + " " + usuario.getApellido());
        System.out.println("? ID Usuario: " + usuario.getIdUsuario());

        // Obtener roles desde la tabla intermedia con una consulta nativa
        List<GrantedAuthority> authorities = new ArrayList<>();

        try {
            List<String> roleNames = usuarioRepository.findRoleNamesByUserId(usuario.getIdUsuario());
            if (roleNames != null && !roleNames.isEmpty()) {
                for (String rn : roleNames) {
                    if (rn != null && !rn.isEmpty()) {
                        String name = rn.trim().toUpperCase();
                        authorities.add(new SimpleGrantedAuthority("ROLE_" + name));
                    }
                }
                System.out.println("? Authorities asignadas desde la BD (nombres): " + authorities.size());
            }
        } catch (Exception e) {
            System.err.println("? ERROR consultando roles desde DB para usuario " + usuario.getIdUsuario() + ": " + e.getMessage());
        }

        // Si no hay roles en BD, fallback a la lógica anterior
        if (authorities.isEmpty()) {
            // ✅ Verificar si es el usuario vigilante específico (cédula 1000856999)
            if ("1000856999".equals(cedula)) {
                authorities.add(new SimpleGrantedAuthority("ROLE_VIGILANTE"));
                System.out.println("? Asignado ROLE_VIGILANTE para usuario: " + cedula);
            }
            // Verificar si es instructor (no tiene ficha asignada) o aprendiz
            else if (usuario.getFicha() == null) {
                // Probablemente sea instructor o coordinador
                authorities.add(new SimpleGrantedAuthority("ROLE_INSTRUCTOR"));
                System.out.println("? Asignado ROLE_INSTRUCTOR por defecto (sin ficha)");
            } else {
                // Tiene ficha, probablemente sea aprendiz
                authorities.add(new SimpleGrantedAuthority("ROLE_APRENDIZ"));
                System.out.println("? Asignado ROLE_APRENDIZ por defecto (con ficha)");
            }

            System.out.println("? Total authorities (fallback): " + authorities.size());
        }

        return new CustomUserDetails(
                usuario.getCedula(),
                usuario.getPassword(),
                authorities,
                usuario
        );
    }
}