package com.tallerjava.taller.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RedirectController {

    @GetMapping("/redirect")
    public String redirectByRole(Authentication authentication) {

        String rol = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("");

        switch (rol) {
            case "ROLE_APRENDIZ":
                return "redirect:/aprendiz/asistencia";

            case "ROLE_INSTRUCTOR":
                return "redirect:/instructor/gestionar-asistencia"; // ✅ CAMBIADO

            case "ROLE_COORDINADOR":
                return "redirect:/coordinador/home";

            default:
                return "redirect:/home";
        }
    }
}