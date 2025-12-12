package com.tallerjava.taller.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NotificationsPageController {

    @GetMapping("/coordinador/notifications")
    public String notificationsPage() {
        return "coordinador/notifications";
    }
}
