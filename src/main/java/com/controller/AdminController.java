package com.controller;

import com.service.MatriculaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @Autowired
    private MatriculaService matriculaService;

    // Página inicial do admin
    @GetMapping("/admin/home")
    public String exibirHomeAdmin() {
        return "admin/home"; // templates/admin/home.html
    }

    // Listar todas as matrículas (admin)
    @GetMapping("/admin/matriculas")
    public String listarMatriculasAdmin(Model model) {
        model.addAttribute("matriculas", matriculaService.buscarTodas());
        return "admin/matriculas"; // templates/admin/matriculas-admin.html
    }
}
