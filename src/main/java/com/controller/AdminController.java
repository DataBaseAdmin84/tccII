package com.controller;

import com.service.CursoService;
import com.service.MatriculaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @Autowired
    private MatriculaService matriculaService;
    @Autowired
    private CursoService cursoService;

    // Página inicial do admin
    @GetMapping("/admin/home")
    public String exibirHomeAdmin() {
        return "home"; // templates/admin/home.html
    }

    // Listar todas as matrículas (admin)
    @GetMapping("/admin/matriculas")
    public String listarMatriculasAdmin(Model model) {
        model.addAttribute("matriculas", matriculaService.buscarTodas());
        return "admin/matriculas"; // templates/admin/matriculas-admin.html
    }
    @GetMapping("/admin/matriculas/novo")
    public String novaMatriculaForm(Model model) {
        // adiciona atributos se necessário
        return "admin/formmatricula"; //  correto
    }


}
