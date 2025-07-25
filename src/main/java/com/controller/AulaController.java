package com.controller;

import com.dto.ArquivoDTO;
import com.dto.AulaDTO;
import com.model.Arquivo;
import com.model.Aula;
import com.model.Usuario;
import com.repository.AulaRepository;
import com.repository.CursoRepository;
import com.service.AulaService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Optional;

@Controller
public class AulaController {
    private static final Logger log = LoggerFactory.getLogger(AulaController.class);
    @Autowired
    CursoRepository cursoRepository;

    @Autowired
    AulaRepository aulaRepository;

    @Autowired
    AulaService aulaService;

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
    public String salvarAula(@ModelAttribute AulaDTO aulaDTO, Model model, HttpSession session) {
        try {
            if(aulaDTO.getId() != null)
                editarAula(aulaDTO.getId(), aulaDTO, model, session);

            var curso = cursoRepository.findById(aulaDTO.getIdCurso());
            Aula aula = AulaDTO.toModel(aulaDTO);
            curso.ifPresent(aula::setCurso);

            aulaRepository.save(aula);
            model.addAttribute("aula", AulaDTO.toDto(aula));
            model.addAttribute("cursoId", aula.getCurso().getId());
            model.addAttribute("usuarioLogado", session.getAttribute("usuarioLogado"));
            return "preparacaoaula";

        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao preencher formulário de aula "+e.getLocalizedMessage());
            log.error(e.getLocalizedMessage());
        }
        return "redirect:/preparacaoaula";
    }

    @GetMapping("/aula/editar/{id}")
    public String editarAula(@PathVariable("id") Long id, AulaDTO aulaDTO, Model model, HttpSession session) {
        try {
            Optional<Aula> aulaOptional = aulaRepository.findById(id);

            if (aulaOptional.isPresent()) {
                Aula aula = aulaOptional.get();
                aulaDTO = AulaDTO.toDto(aula);

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
                return "redirect:/cursos";
            }
        } catch (Exception e) {
            log.error("Erro ao carregar aula para edição: {}", e.getMessage());
            return "redirect:/cursos";
        }
    }

    @PostMapping("/aula/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        try {
            var aulaOpt = aulaRepository.findById(id);
            if(aulaOpt.isPresent()){
                aulaOpt.ifPresent(aula -> aulaService.removerVinculosPorAula(aula));
                aulaRepository.delete(aulaOpt.get());
                return "redirect:/cursos";
            }
            return "redirect:/cursos";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/visualizar/{id}")
    public ModelAndView visualizarAula(@PathVariable("id") Long id) {
        Optional<Aula> aulaOpt = aulaRepository.findById(id);

        if (aulaOpt.isPresent()) {
            Aula aula = aulaOpt.get();
            ModelAndView mv = new ModelAndView("visualizaraula");

            mv.addObject("aula", AulaDTO.toDto(aula));

            if (aula.getCurso() != null) {
                mv.addObject("cursoId", aula.getCurso().getId());
            }

            return mv;
        } else {
            log.warn("Tentativa de visualizar aula com ID inexistente: {}", id);
            return new ModelAndView("redirect:/cursos");
        }
    }
}
