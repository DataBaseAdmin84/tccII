package com.controller;

import com.model.Usuario;
import com.repository.MatriculaRepository;
import com.service.MatriculaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MatriculaController {

    @Autowired
    private MatriculaService matriculaService;

    @Autowired
    private MatriculaRepository matriculaRepository;

    @PostMapping("/matricula/novo")
    public String salvar(@RequestParam Long cursoId, HttpSession session) {
        var usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        matriculaService.salvarMatricula(usuarioLogado.getId(), cursoId);
        return "redirect:/cursos";
    }

    @GetMapping("/matricula/excluir/{id}")
    public String excluirMatricula(@PathVariable("id") Long cursoId, HttpSession session) {
        var usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        matriculaService.excluirPorUsuarioCurso(cursoId, usuarioLogado.getId());
        return "redirect:/cursos";
    }
}
