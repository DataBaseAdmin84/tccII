package com.controller;

import com.dto.MatriculaDTO;
import com.model.Matricula;
import com.service.CursoService;
import com.service.MatriculaService;
import com.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/matriculas")
public class MatriculaController {

    @Autowired
    private MatriculaService matriculaService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private CursoService cursoService;

    // Listar todas as matrículas
    @GetMapping
    public String listarMatriculas(Model model) {
        model.addAttribute("matriculas", matriculaService.buscarTodas());
        return "matriculas";
    }

    // Formulário de nova matrícula
    @GetMapping("/novo")
    public String novaMatricula(Model model) {
        model.addAttribute("matricula", new MatriculaDTO());
        model.addAttribute("usuarios", usuarioService.listarEntidades()); // Certo: listarEntidades()
        model.addAttribute("cursos", cursoService.listarCursos());
        return "formmatricula";
    }

    @PostMapping("/salvar")
    public String salvarMatricula(@ModelAttribute MatriculaDTO matriculaDTO, RedirectAttributes redirect) {
        matriculaService.salvarMatricula(matriculaDTO);
        redirect.addFlashAttribute("msg", "Matrícula salva com sucesso!");
        return "redirect:/matriculas"; // ← redireciona para a tela correta do admin
    }



    // Editar matrícula
    @GetMapping("/editar/{id}")
    public String editarMatricula(@PathVariable Long id, Model model) {
        Matricula matricula = matriculaService.buscarPorId(id);

        if (matricula == null) {
            return "redirect:/matriculas"; // Se não encontrar, redireciona
        }

        MatriculaDTO dto = new MatriculaDTO();
        dto.setId(matricula.getId());

        if (matricula.getUsuario() != null) {
            dto.setUsuarioId(matricula.getUsuario().getId());
        }
        if (matricula.getCurso() != null) {
            dto.setCursoId(matricula.getCurso().getId());
        }
        dto.setDataMatricula(matricula.getDataMatricula());

        model.addAttribute("matricula", dto);
        model.addAttribute("usuarios", usuarioService.listarEntidades());
        model.addAttribute("cursos", cursoService.listarCursos());
        return "formmatricula";
    }

    // Excluir matrícula
    @GetMapping("/excluir/{id}")
    public String excluirMatricula(@PathVariable Long id) {
        matriculaService.excluirMatricula(id);
        return "redirect:/matriculas";
    }
}
