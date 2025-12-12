package com.tallerjava.taller.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public void handleException(Exception ex) {
        // Solo registrar la excepción sin intentar devolver respuesta
        System.err.println("=== ERROR EN APLICACIÓN ===");
        ex.printStackTrace();
        System.err.println("=== FIN ERROR ===");
        // No devolver nada, dejar que Spring maneje el error por defecto
    }
}
