package com.tallerjava.taller.config;

import com.tallerjava.taller.security.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.csrf.CsrfToken;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // ✅ Handler moderno para CSRF
        CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
        requestHandler.setCsrfRequestAttributeName("_csrf"); // Para Thymeleaf
        
        http
            // ✅ CSRF CONFIGURACIÓN DEFINITIVA
            .csrf(csrf -> csrf
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .csrfTokenRequestHandler(requestHandler)
                .sessionAuthenticationStrategy((authentication, request, response) -> {
                    // Asegurar que CSRF token se refresque después del login
                    CsrfToken csrfToken = (CsrfToken) request.getAttribute("_csrf");
                    if (csrfToken != null) {
                        response.setHeader(csrfToken.getHeaderName(), csrfToken.getToken());
                    }
                })
            )
            
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/css/**", "/js/**", "/images/**", "/uploads/**").permitAll()
                .requestMatchers("/redirect").authenticated()
                .requestMatchers("/aprendiz/**").hasAnyRole("APRENDIZ")
                .requestMatchers("/instructor/**").hasAnyRole("INSTRUCTOR")
                .requestMatchers("/coordinador/**").hasAnyRole("COORDINADOR")
                .requestMatchers("/vigilante/**").hasAnyRole("VIGILANTE")
                .anyRequest().authenticated()
            )
            
            .userDetailsService(customUserDetailsService)
            
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("cedula")
                .passwordParameter("password")
                .defaultSuccessUrl("/redirect", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID", "XSRF-TOKEN")
                .clearAuthentication(true)
                .permitAll()
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}