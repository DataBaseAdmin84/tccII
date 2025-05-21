package com.controller;

import com.dto.UsuarioDTO;
import com.model.Curso;
import com.model.Perfil;
import com.model.Usuario;
import com.service.CursoService;
import com.service.MatriculaService;
import com.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/aluno")
public class AlunoController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private CursoService cursoService;

    @Autowired
    private MatriculaService matriculaService;

    @GetMapping("/novo")
    public String novoAlunoForm(Model model) {
        model.addAttribute("aluno", new UsuarioDTO());
        return "aluno/formaluno";
    }

    @PostMapping("/salvar")
    public String salvarAluno(@ModelAttribute("aluno") UsuarioDTO dto) {
        dto.setPerfil(Perfil.ALUNO);
        usuarioService.salvarUsuario(dto);
        return "redirect:/usuarios";
    }

    @GetMapping("/home")
    public String homeAluno() {
        return "aluno/home";
    }

    @GetMapping("/cursos")
    public String listarCursos(Model model) {
        List<Curso> cursos = cursoService.listarTodos();
        model.addAttribute("cursos", cursos);
        return "aluno/cursos";
    }

    @PostMapping("/matricular/{id}")
    public String matricular(@PathVariable Long id, HttpSession session, RedirectAttributes redirect) {
        Usuario aluno = (Usuario) session.getAttribute("usuarioLogado");
        if (aluno == null) {
            redirect.addFlashAttribute("erro", "Sessão expirada. Faça login novamente.");
            return "redirect:/";
        }

        matriculaService.matricularAlunoEmCurso(aluno.getId(), id);
        redirect.addFlashAttribute("msg", "Matrícula realizada com sucesso!");
        return "redirect:/aluno/cursos";
    }

    @GetMapping("/matriculados")
    public String cursosMatriculados(Model model, HttpSession session, RedirectAttributes redirect) {
        Usuario aluno = (Usuario) session.getAttribute("usuarioLogado");
        if (aluno == null) {
            redirect.addFlashAttribute("erro", "Sessão expirada. Faça login novamente.");
            return "redirect:/";
        }

        model.addAttribute("matriculas", matriculaService.buscarMatriculasPorAluno(aluno.getId()));
        return "aluno/matriculados";
    }
}
