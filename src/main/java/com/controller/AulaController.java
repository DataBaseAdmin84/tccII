package com.controller;

import com.dto.ArquivoDTO;
import com.dto.AulaDTO;
import com.dto.UsuarioDTO;
import com.model.Arquivo;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Optional;

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
            return "redirect:/cursos";

        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao preencher formulário de aula "+e.getLocalizedMessage());
            log.error(e.getLocalizedMessage());
        }
        return "redirect:/cursos";
    }

    @GetMapping("/aula/editar/{id}")
    public String editarAula(@PathVariable("id") Long id, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            Optional<Aula> aulaOptional = aulaRepository.findById(id);

            if (aulaOptional.isPresent()) {
                Aula aula = aulaOptional.get();
                AulaDTO aulaDTO = new AulaDTO();
                aulaDTO.setId(aula.getId());
                //TODO ADD TITULO EM AULAS
                aulaDTO.setTitulo("Valor Padrão");
                aulaDTO.setDescricao(aula.getDescricao());
                aulaDTO.setIdCurso(aula.getCurso().getId());

                var listArquivos = new ArrayList<ArquivoDTO>();
                for(Arquivo arquivo: aulaOptional.get().getArquivos()){
                    var dto = ArquivoDTO.toDto(arquivo);
                    listArquivos.add(dto);
                }
                aulaDTO.setArquivos(listArquivos);

                model.addAttribute("aula", aulaDTO);
                model.addAttribute("cursoId", aula.getCurso().getId());
                model.addAttribute("usuarioLogado", session.getAttribute("usuarioLogado"));

                return "preparacaoaula";
            } else {
                redirectAttributes.addFlashAttribute("erro", "Aula não encontrada com o ID: " + id);
                return "redirect:/cursos";
            }
        } catch (Exception e) {
            log.error("Erro ao carregar aula para edição: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("erro", "Ocorreu um erro ao tentar editar a aula.");
            return "redirect:/cursos";
        }
    }
}
