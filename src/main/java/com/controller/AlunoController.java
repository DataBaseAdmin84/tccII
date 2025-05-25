package com.controller;

import com.dto.MatriculaDTO;
import com.dto.UsuarioDTO;
import com.model.Curso;
import com.model.Perfil;
import com.model.Usuario;
import com.model.Matricula;
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
import java.util.stream.Collectors;

@Controller
@RequestMapping("/aluno")
public class AlunoController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private CursoService cursoService;

    @Autowired
    private MatriculaService matriculaService;

    // Formulário para novo aluno
    @GetMapping("/novo")
    public String novoAlunoForm(Model model) {
        model.addAttribute("aluno", new UsuarioDTO());
        return "aluno/formaluno";
    }

    // Salvar novo aluno
    @PostMapping("/salvar")
    public String salvarAluno(@ModelAttribute("aluno") UsuarioDTO dto) {
        dto.setPerfil(Perfil.ALUNO);
        usuarioService.salvarUsuario(dto);
        return "redirect:/usuarios";
    }

    // Página inicial do aluno
    @GetMapping("/home")
    public String homeAluno() {
        return "aluno/home";
    }

    // Listar todos os cursos disponíveis + cursos já matriculados
    @GetMapping("/cursos")
    public String listarCursos(Model model, HttpSession session, RedirectAttributes redirect) {
        Usuario aluno = (Usuario) session.getAttribute("usuarioLogado");
        if (aluno == null) {
            redirect.addFlashAttribute("erro", "Sessão expirada. Faça login novamente.");
            return "redirect:/";
        }

        List<Curso> cursos = cursoService.listarTodos();

        // Lista de IDs dos cursos em que o aluno já está matriculado
        List<Long> cursosMatriculados = matriculaService.buscarMatriculasPorAluno(aluno.getId())
                .stream()
                .map(m -> m.getCurso().getId())
                .collect(Collectors.toList());

        model.addAttribute("cursos", cursos);
        model.addAttribute("cursosMatriculados", cursosMatriculados);

        return "aluno/cursos";
    }

    // Matricular o aluno em um curso específico
    @PostMapping("/matricular/{id}")
    public String matricular(@PathVariable Long id, HttpSession session, RedirectAttributes redirect) {
        Usuario aluno = (Usuario) session.getAttribute("usuarioLogado");
        if (aluno == null) {
            redirect.addFlashAttribute("erro", "Sessão expirada. Faça login novamente.");
            return "redirect:/";
        }

        matriculaService.matricularAlunoEmCurso(aluno.getId(), id);
        redirect.addFlashAttribute("msg", "Matrícula realizada com sucesso!");

        // Redireciona direto para a tela de aulas do curso matriculado
        return "redirect:/aulas/curso/" + id;
    }

    // Listar cursos nos quais o aluno está matriculado
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
    @GetMapping("/matriculas/novo")
    public String novaMatriculaForm(Model model) {
        model.addAttribute("matricula", new MatriculaDTO());
        model.addAttribute("usuarios", usuarioService.buscarTodos());
        model.addAttribute("cursos", cursoService.listarTodos());
        return "aluno/formmatricula";
    }
    @GetMapping("/aluno/matriculados")
    public String listarMeusCursos(Model model, HttpSession session) {
        Usuario aluno = (Usuario) session.getAttribute("usuarioLogado");

        if (aluno == null) {
            return "redirect:/";
        }

        model.addAttribute("matriculas", matriculaService.buscarMatriculasDoAluno(aluno));
        return "aluno/matriculados";    }




}
    