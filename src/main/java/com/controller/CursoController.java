package com.controller;

import com.dto.CursoDTO;
import com.enums.PerfilUsuario;
import com.model.Usuario;
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
import java.util.List;

@Controller
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private S3Service s3Service;

    @GetMapping("/curso/novo")
    public String novoCursoForm(Model model) {
        model.addAttribute("curso", new CursoDTO());
        model.addAttribute("professores", PerfilUsuario.PROFESSOR.getCodigo());
        return "formcurso";
    }

    @PostMapping("/curso/salvar")
    public String salvarCurso(@ModelAttribute CursoDTO cursoDTO,
                              @RequestParam("professorId") Long professorId,
                              @RequestParam("arquivoPdf") MultipartFile arquivoPdf,
                              RedirectAttributes redirectAttributes) throws IOException {
        Usuario professor = usuarioService.buscarEntidadePorId(professorId);

        // Fazer upload do PDF para S3 (ou outro serviço)
        if (!arquivoPdf.isEmpty()) {
            String urlPdf = s3Service.uploadArquivo(arquivoPdf);
//            cursoDTO.setUrlPdf(urlPdf);
        }

        cursoService.salvarCurso(cursoDTO, professor);
        redirectAttributes.addFlashAttribute("mensagem", "Curso salvo com sucesso!");
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
