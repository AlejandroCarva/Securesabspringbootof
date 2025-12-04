package com.tallerjava.taller.security;

import com.tallerjava.taller.model.Usuario;
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
import java.util.stream.Collectors;

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

        // Obtener roles REALES del usuario
        List<GrantedAuthority> authorities = new ArrayList<>();
        
        if (usuario.getRoles() != null && !usuario.getRoles().isEmpty()) {
            System.out.println("? Roles encontrados en BD:");
            authorities = usuario.getRoles().stream()
                    .map(role -> {
                        String roleName = "ROLE_" + role.getName().toUpperCase();
                        System.out.println("? - " + role.getName() + " -> " + roleName);
                        return new SimpleGrantedAuthority(roleName);
                    })
                    .collect(Collectors.toList());
        } else {
            // Si no tiene roles asignados, asignar según el tipo de usuario
            System.out.println("? No se encontraron roles en BD. Asignando por defecto...");
            
            // Verificar si es instructor (no tiene ficha asignada)
            if (usuario.getFicha() == null) {
                // Probablemente sea instructor o coordinador
                authorities.add(new SimpleGrantedAuthority("ROLE_INSTRUCTOR"));
                System.out.println("? Asignado ROLE_INSTRUCTOR por defecto (sin ficha)");
            } else {
                // Tiene ficha, probablemente sea aprendiz
                authorities.add(new SimpleGrantedAuthority("ROLE_APRENDIZ"));
                System.out.println("? Asignado ROLE_APRENDIZ por defecto (con ficha)");
            }
        }

        System.out.println("? Total authorities: " + authorities.size());
        
        return new CustomUserDetails(
                usuario.getCedula(),
                usuario.getPassword(),
                authorities,
                usuario
        );
    }
}