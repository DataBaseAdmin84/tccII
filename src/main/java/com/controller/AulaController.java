package com.controller;

import com.dto.AulaDTO;
import com.model.Aula;
import com.model.Usuario;
import com.service.AulaService;
import com.service.CursoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AulaController {

    private final AulaService aulaService;
    private final CursoService cursoService;

    public AulaController(AulaService aulaService, CursoService cursoService) {
        this.aulaService = aulaService;
        this.cursoService = cursoService;
    }

    @PostMapping("/aula/novo")
    public String listarAulasProfessor(Model model, HttpSession session) {
        Usuario professor = (Usuario) session.getAttribute("usuarioLogado");
        model.addAttribute("aulas", new AulaDTO());
        return "preparacaoaula";
    }
}
