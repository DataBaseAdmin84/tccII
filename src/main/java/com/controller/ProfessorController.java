package com.controller;

import com.dto.CursoDTO;
import com.model.Usuario;
import com.service.CursoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        //List<CursoDTO> cursos = cursoService.listarCursosPorProfessor(professor);
       // model.addAttribute("cursos", cursos);
        return "professor/home";
    }


    @GetMapping("/professor/curso/novo")
    public String exibirFormularioCurso(Model model) {
        CursoDTO curso = new CursoDTO();
        model.addAttribute("curso", curso);
        model.addAttribute("titulo", "Novo Curso");
        model.addAttribute("descricao", "Preencha os dados do curso abaixo:");
        model.addAttribute("acao", "Salvar");
        return "professor/formcurso";
    }

    @PostMapping("/salvar")
    public String salvarCurso(@ModelAttribute("curso") CursoDTO dto,
                              @RequestParam("arquivoPdf") MultipartFile arquivo,
                              HttpSession session) {

        Usuario professor = (Usuario) session.getAttribute("usuarioLogado");

        if (professor == null || professor.getId() == null) {
            return "redirect:/";
        }

        try {
            if (!arquivo.isEmpty()) {
                // Salvar arquivo no S3 e obter URL
                String nomeArquivo = arquivo.getOriginalFilename();
                String s3Url = "https://s3.us-east-2.amazonaws.com/cursosctcc/" + nomeArquivo;


                //dto.setUrlPdf(s3Url);
            }


            cursoService.salvarCurso(dto, professor);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/professor/home";
    }



    @GetMapping("/professor/curso/editar/{id}")
    public String editarCurso(@PathVariable Long id, Model model, HttpSession session) {
        Usuario professor = (Usuario) session.getAttribute("usuarioLogado");
        CursoDTO curso = cursoService.buscarPorId(id);

//        // verifica se o curso pertence a esse professor
//        if (!curso.getProfessorId().equals(professor.getId())) {
//            return "redirect:/professor/home";
//        }

        model.addAttribute("curso", curso);
        return "professor/formcurso";
    }

    // Excluir curso
    @GetMapping("/professor/curso/excluir/{id}")
    public String excluirCurso(@PathVariable Long id, HttpSession session, RedirectAttributes redirect) {
        Usuario professor = (Usuario) session.getAttribute("usuarioLogado");

        if (professor == null) {
            redirect.addFlashAttribute("erro", "Sessão expirada. Faça login novamente.");
            return "redirect:/";
        }

        try {
           // cursoService.excluirCursoSePertencerAoProfessor(id, professor);
            redirect.addFlashAttribute("msg", "Curso excluído com sucesso!");
        } catch (Exception e) {
            redirect.addFlashAttribute("erro", "Erro ao excluir curso: " + e.getMessage());
        }

        return "redirect:/professor/home";
    }

    // Painel alternativo
    @GetMapping("/professor/painel")
    public String painelProfessor(HttpSession session, Model model) {
        Usuario professor = (Usuario) session.getAttribute("usuarioLogado");
        if (professor == null) return "redirect:/";

        //List<CursoDTO> cursos = cursoService.listarCursosPorProfessor(professor);
        //model.addAttribute("cursos", cursos);
        return "professor/painel";
    }

}
