package com.controller;

import com.dto.CursoDTO;
import com.model.Curso;
import com.model.Usuario;
import com.service.CursoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class ProfessorController {

    @Autowired
    private CursoService cursoService;

    // Página inicial do professor
    @GetMapping("/professor/home")
    public String listarCursosDoProfessor(HttpSession session, Model model) {
        Usuario professor = (Usuario) session.getAttribute("usuarioLogado");
        if (professor == null) return "redirect:/";

        List<Curso> cursos = cursoService.listarTodos(); // ou cursoService.listarCursosPorProfessor(professor);
        model.addAttribute("cursos", cursos);
        return "professor/cursos";
    }

    // Formulário de novo curso
    @GetMapping("/professor/curso/novo")
    public String exibirFormularioCurso(Model model) {
        model.addAttribute("curso", new CursoDTO());
        return "professor/formcurso";
    }

    // Salvar novo curso (com DTO)
    @PostMapping("/professor/curso/salvar")
    public String salvarCurso(@ModelAttribute("curso") CursoDTO dto, HttpSession session) {
        Usuario professor = (Usuario) session.getAttribute("usuarioLogado");
        if (professor == null || professor.getId() == null) {
            return "redirect:/";
        }

        cursoService.salvarCurso(dto, professor);
        return "redirect:/professor/home";
    }
}
