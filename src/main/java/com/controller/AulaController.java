package com.controller;

import com.model.Aula;
import com.model.Usuario;
import com.service.AulaService;
import com.service.CursoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/aulas")
public class AulaController {

    private final AulaService aulaService;
    private final CursoService cursoService;

    public AulaController(AulaService aulaService, CursoService cursoService) {
        this.aulaService = aulaService;
        this.cursoService = cursoService;
    }

    @GetMapping("/professor")
    public String listarAulasProfessor(Model model, HttpSession session) {
        Usuario professor = (Usuario) session.getAttribute("usuarioLogado");
        //model.addAttribute("aulas", aulaService.listarPorProfessor(professor.getId()));
        return "professor/aulas";
    }

    @GetMapping("/professor/nova")
    public String formNovaAula(Model model, HttpSession session) {
        Usuario professor = (Usuario) session.getAttribute("usuarioLogado");
        model.addAttribute("aula", new Aula());
        model.addAttribute("cursos", cursoService.listarCursosPorProfessor(professor));
        return "professor/aula-form";
    }

    @PostMapping("/professor/salvar")
    public String salvarAula(@ModelAttribute Aula aula, HttpSession session) {
        Usuario professor = (Usuario) session.getAttribute("usuarioLogado");
        aulaService.salvar(aula);
        return "redirect:/aulas/professor";
    }

    @GetMapping("/curso/{id}")
    public String listarAulasCurso(@PathVariable Long id, Model model) {
        model.addAttribute("aulas", aulaService.listarPorCurso(id));
        model.addAttribute("cursoId", id);
        return "aluno/aulas";
    }
}

// teste commit