package com.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/admin/home")
    public String exibirHomeAdmin() {
        return "admin/home"; // busca templates/admin/home.html
    }
}
