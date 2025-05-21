package com.controller;

import com.model.Aula;
import com.model.Curso;

import com.model.Usuario;
import com.service.AulaService;
import com.service.CursoService;
import com.service.UsuarioService;
import jakarta.servlet.http.HttpSession;

import com.repository.CursoRepository;
import com.service.AulaService;
import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/aulas")
public class AulaController {

    private final AulaService aulaService;
    private final CursoService cursoService;
    private final UsuarioService usuarioService;
    private final CursoRepository cursoRepository;

    public AulaController(AulaService aulaService, CursoService cursoService, UsuarioService usuarioService, CursoRepository cursoRepository) {
        this.aulaService = aulaService;
        this.cursoService = cursoService;
        this.usuarioService = usuarioService;
        this.cursoRepository = cursoRepository;
    }

    @GetMapping("/professor")
    public String listarAulasProfessor(Model model, HttpSession session) {
        Usuario professor = (Usuario) session.getAttribute("usuarioLogado");
        model.addAttribute("aulas", aulaService.listarPorProfessor(professor.getId()));
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
        aula.setProfessor(professor);
        aulaService.salvar(aula);
        return "redirect:/aulas/professor";
    }

    @GetMapping("/curso/{id}")
    public String listarAulasCurso(@PathVariable Long id, Model model) {
        model.addAttribute("aulas", aulaService.listarPorCurso(id));
        model.addAttribute("cursoId", id);
        return "aluno/aulas";
    }

    @GetMapping("/novo/{cursoId}")
    public String novaAulaForm(@PathVariable Long cursoId, Model model) {
        Curso curso = cursoRepository.findById(cursoId).orElseThrow();
        Aula aula = new Aula();
        aula.setCurso(curso);
        model.addAttribute("aula", aula);
        return "professor/formularioaula";
    }

    @PostMapping("/salvar")
    public String salvarAula(@ModelAttribute @Valid Aula aula) {
        aulaService.salvar(aula);
        return "redirect:/professor/cursos";
    }

    @GetMapping("/curso/listar/{cursoId}")
    public String listarAulasPorCurso(@PathVariable Long cursoId, Model model) {
        Curso curso = cursoRepository.findById(cursoId).orElseThrow();
        model.addAttribute("aulas", aulaService.listarPorCurso(curso));
        return "aluno/aulas";
    }
}

