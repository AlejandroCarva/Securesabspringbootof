package com.tallerjava.taller.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class CsrfControllerAdvice {
    
    @ModelAttribute("_csrf")
    public CsrfToken getCsrfToken(HttpServletRequest request) {
        CsrfToken csrfToken = (CsrfToken) request.getAttribute("_csrf");
        System.out.println("🛡️ CsrfControllerAdvice - CSRF Token disponible: " + 
                          (csrfToken != null ? "SÍ" : "NO"));
        if (csrfToken != null) {
            System.out.println("  - Parameter name: " + csrfToken.getParameterName());
            System.out.println("  - Token: " + 
                (csrfToken.getToken() != null ? 
                 csrfToken.getToken().substring(0, Math.min(10, csrfToken.getToken().length())) + "..." : 
                 "null"));
        }
        return csrfToken;
    }
}