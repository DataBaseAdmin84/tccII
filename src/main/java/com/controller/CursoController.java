package com.controller;

import com.dto.CursoDTO;
import com.enums.PerfilUsuario;
import com.model.Curso;
import com.model.Usuario;
import com.repository.CursoRepository;
import com.service.CursoService;
import com.service.S3Service;
import com.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CursoController {

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private CursoService cursoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private S3Service s3Service;

    @PostMapping("/curso/novo")
    public String salvar(@ModelAttribute CursoDTO cursoDTO,
                              @RequestParam("arquivoPdf") MultipartFile arquivoPdf) throws IOException {
        Curso curso = new Curso();
        curso.setNome(cursoDTO.getNome());
        curso.setDescricao(cursoDTO.getDescricao());
        curso.setUrlImagem(cursoDTO.getDescricao());
        cursoRepository.save(curso);
        curso.setMatriculas(new ArrayList<>());
        curso.setMatriculas(new ArrayList<>());
        //TODO salvar o list de matricula e salvar o list de usuario.
        return "redirect:/cursos";
    }

    @GetMapping("/cursos")
    public String listarCursos(Model model) {
        List<CursoDTO> cursos = cursoService.listarCursos();
        model.addAttribute("cursos", cursos);
        return "admin/cursos";
    }

    @GetMapping("/curso/editar/{id}")
    public String editarCurso(@PathVariable Long id, Model model) {
        CursoDTO cursoDTO = cursoService.buscarPorId(id);
        model.addAttribute("curso", cursoDTO);
        model.addAttribute("professores", PerfilUsuario.PROFESSOR.getCodigo());
        return "formcurso";
    }

    @GetMapping("/curso/excluir/{id}")
    public String excluirCurso(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            cursoService.excluirPorId(id);
            redirect.addFlashAttribute("msg", "Curso excluído com sucesso!");
        } catch (RuntimeException e) {
            redirect.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/cursos";
    }
}
