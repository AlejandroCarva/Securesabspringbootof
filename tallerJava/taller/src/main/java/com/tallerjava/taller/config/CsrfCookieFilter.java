package com.tallerjava.taller.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro para garantizar que las cookies CSRF se establezcan correctamente
 * y sean accesibles para Thymeleaf y JavaScript.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CsrfCookieFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                   HttpServletResponse response,
                                   FilterChain filterChain)
            throws ServletException, IOException {
        
        // Obtener el token CSRF del request
        CsrfToken csrfToken = (CsrfToken) request.getAttribute("_csrf");
        
        if (csrfToken != null) {
            // Debug: Verificar que el token esté presente
            System.out.println("🔒 CSRF Cookie Filter:");
            System.out.println("  - Token: " + (csrfToken.getToken() != null ? 
                csrfToken.getToken().substring(0, Math.min(10, csrfToken.getToken().length())) + "..." : "null"));
            System.out.println("  - Parameter name: " + csrfToken.getParameterName());
            System.out.println("  - Header name: " + csrfToken.getHeaderName());
            
            // Agregar headers para que JavaScript pueda acceder al token si es necesario
            response.setHeader("X-CSRF-PARAM", csrfToken.getParameterName());
            response.setHeader("X-CSRF-TOKEN", csrfToken.getToken());
            
            // Para CookieCsrfTokenRepository, la cookie se establece automáticamente
            // Pero podemos verificar que esté presente
            String csrfCookie = request.getHeader("Cookie");
            if (csrfCookie != null && csrfCookie.contains("XSRF-TOKEN")) {
                System.out.println("  - CSRF cookie presente en request");
            }
        } else {
            System.out.println("⚠️ CSRF Token NO encontrado en el request");
        }
        
        filterChain.doFilter(request, response);
    }
    
    @Override
    protected boolean shouldNotFilterAsyncDispatch() {
        return false;
    }
    
    @Override
    protected boolean shouldNotFilterErrorDispatch() {
        return false;
    }
}