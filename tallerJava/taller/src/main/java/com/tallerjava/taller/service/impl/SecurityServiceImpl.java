package com.tallerjava.taller.service.impl;

import com.tallerjava.taller.security.CustomUserDetails;
import com.tallerjava.taller.service.ISecurityService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityServiceImpl implements ISecurityService {

    @Override
    public Integer getIdUsuarioActual() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                Object principal = authentication.getPrincipal();
                
                System.out.println("🔐 SECURITY SERVICE - DEBUG:");
                System.out.println("   - Authentication name: " + authentication.getName());
                System.out.println("   - Principal type: " + principal.getClass().getSimpleName());
                
                // ✅ IMPORTANTE: Verificar si es CustomUserDetails
                if (principal instanceof CustomUserDetails) {
                    CustomUserDetails userDetails = (CustomUserDetails) principal;
                    
                    System.out.println("   ✅ CustomUserDetails encontrado!");
                    System.out.println("      - ID Usuario: " + userDetails.getIdUsuario());
                    System.out.println("      - Nombre: " + userDetails.getNombreCompleto());
                    System.out.println("      - Username: " + userDetails.getUsername());
                    System.out.println("      - Authorities: " + userDetails.getAuthorities().size());
                    
                    Integer id = userDetails.getIdUsuario();
                    System.out.println("   🔄 Devolviendo ID: " + id);
                    return id;
                } 
                // ✅ También verificar si es el User estándar de Spring (por seguridad)
                else if (principal instanceof org.springframework.security.core.userdetails.User) {
                    System.out.println("   ⚠️ Es User estándar de Spring Security");
                    System.out.println("   ❌ No se puede obtener el ID del usuario");
                } 
                else if (principal instanceof String) {
                    System.out.println("   ⚠️ Principal es String: " + principal);
                    System.out.println("   ℹ️ Esto puede pasar si el usuario es 'anonymousUser'");
                } 
                else {
                    System.out.println("   ❌ Tipo de principal no reconocido: " + principal.getClass().getName());
                }
            } else {
                System.out.println("🔐 No hay autenticación activa o usuario no autenticado");
            }
        } catch (Exception e) {
            System.out.println("❌ ERROR en getIdUsuarioActual: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("🔐 Devolviendo null (usuario no autenticado)");
        return null;
    }
}