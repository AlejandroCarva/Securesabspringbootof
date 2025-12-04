package com.tallerjava.taller.security;

import com.tallerjava.taller.model.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CustomUserDetails extends User {
    private final Usuario usuario;
    private final Integer idUsuario; // ✅ Agregar campo para ID

    public CustomUserDetails(String username, String password, 
                           Collection<? extends GrantedAuthority> authorities, 
                           Usuario usuario) {
        super(username, password, authorities);
        this.usuario = usuario;
        this.idUsuario = usuario.getIdUsuario(); // ✅ Guardar el ID
    }

    public Usuario getUsuario() {
        return usuario;
    }
    
    // ✅ IMPORTANTE: Agregar este método para obtener el ID
    public Integer getIdUsuario() {
        return idUsuario;
    }
    
    // ✅ Método de conveniencia para debugging
    public String getNombreCompleto() {
        return usuario.getNombre() + " " + usuario.getApellido();
    }
}