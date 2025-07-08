package com.controller;

import com.dto.AulaDTO;
import com.dto.UsuarioDTO;
import com.model.Aula;
import com.model.Usuario;
import com.repository.AulaRepository;
import com.repository.CursoRepository;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AulaController {
    private static final Logger log = LoggerFactory.getLogger(AulaController.class);
    @Autowired
    CursoRepository cursoRepository;

    @Autowired
    AulaRepository aulaRepository;

    @GetMapping("/aula/novo")
    public String preencheFormulario(@ModelAttribute AulaDTO aulaDTO, Model model, HttpSession session) {
        try {
            var usuario = (Usuario) session.getAttribute("usuarioLogado");
            model.addAttribute("aula", aulaDTO);
            model.addAttribute("usuarioLogado", usuario);
            return "preparacaoaula";
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao preencher formulário de aula "+e.getLocalizedMessage());
            log.error(e.getLocalizedMessage());
        }
        return "redirect:/cursos";
    }

    @PostMapping("/aula/salvar")
    public String salvarAula(@ModelAttribute AulaDTO aulaDTO, Model model) {
        try {
            var curso = cursoRepository.findById(aulaDTO.getIdCurso());
            Aula aula = new Aula();
            curso.ifPresent(aula::setCurso);
            aula.setDescricao(aulaDTO.getDescricao());

            aulaRepository.save(aula);
            model.addAttribute("aula", aula);
            return "redirect:/curso/preparacaoaula/";

        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao preencher formulário de aula "+e.getLocalizedMessage());
            log.error(e.getLocalizedMessage());
        }
        return "redirect:/cursos";
    }
}
