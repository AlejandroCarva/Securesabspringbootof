package com.tallerjava.taller.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Para acceder a las imágenes desde el navegador
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
        
        // También para recursos estáticos estándar
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }
}