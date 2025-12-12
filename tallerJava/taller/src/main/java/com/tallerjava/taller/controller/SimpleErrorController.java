package com.tallerjava.taller.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SimpleErrorController implements ErrorController {

    @RequestMapping("/error")
    public ResponseEntity<String> handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        int statusCode = status != null ? Integer.parseInt(status.toString()) : 500;
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        String msg = "Error " + statusCode + ": " + (message != null ? message.toString() : "Se produjo un error");
        // No intentar renderizar páginas Thymeleaf aquí – responder texto simple
        return ResponseEntity.status(HttpStatus.valueOf(statusCode)).body(msg);
    }
}

