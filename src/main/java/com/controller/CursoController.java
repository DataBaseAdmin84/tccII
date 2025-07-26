package com.controller;

import com.dataUtil.DataUtils;
import com.dto.CursoDTO;
import com.enums.PerfilUsuario;
import com.filtro.FiltroCurso;
import com.model.Curso;
import com.model.Usuario;
import com.repository.CursoRepository;
import com.service.CursoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

@Controller
public class CursoController {

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private CursoService cursoService;

    @GetMapping("/curso/novo")
    public String exibirFormularioCurso(Model model, HttpSession session) {
        if(session.getAttribute("usuarioLogado") == null) {
            model.addAttribute("erro", "Você precisa estar logado como professor para criar um curso.");
            return "redirect:/";
        }else{
            Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
            if (usuario.getPerfil() != PerfilUsuario.PROFESSOR.getCodigo()) {
                model.addAttribute("erro", "Acesso negado. Apenas professores podem criar cursos.");
                return "redirect:/erro";
            }
            CursoDTO curso = new CursoDTO();
            model.addAttribute("curso", curso);
        }
        return "curso/formcurso";
    }

    @PostMapping("/curso/novo")
    public String salvar(@ModelAttribute CursoDTO cursoDTO, HttpSession session) throws IOException, ParseException {
        var professor = (Usuario) session.getAttribute("usuarioLogado");
        if (professor == null || professor.getPerfil() != PerfilUsuario.PROFESSOR.getCodigo()) {
            cursoDTO.setErro("Você precisa estar logado como professor para criar um curso.");
            return "redirect:/erro";
        }
        Curso curso;
        if (cursoDTO.getId() != null) {
            curso = cursoRepository.findById(cursoDTO.getId())
                    .orElse(new Curso());
        } else {
            curso = new Curso();
        }
        curso.setTitulo(cursoDTO.getTitulo());
        curso.setData(DataUtils.localDateToDate(cursoDTO.getData()));
        curso.setDescricao(cursoDTO.getDescricao());
        curso.setProfessor(professor);
        cursoRepository.save(curso);
        return "redirect:/cursos";
    }

    @GetMapping("/cursos")
    public String listarCursos(CursoDTO dto, Model model, HttpSession session) {
        var filtro = new FiltroCurso();
        var usuario = (Usuario) session.getAttribute("usuarioLogado");
        if(usuario.getPerfil().equals(PerfilUsuario.PROFESSOR.getCodigo()))
            filtro.setIdProfessor(usuario.getId());
        filtro.preencheFiltro(dto);

        var cursos = cursoRepository.findAll(filtro.toSpecification());
        var listCurso = new ArrayList<CursoDTO>();
        for(Curso curso : cursos){
            var c = CursoDTO.toDto(curso);
            if(usuario.getPerfil().equals(PerfilUsuario.ALUNO.getCodigo())){
                var isMatriculado = cursoService.possuiMatricula(curso, usuario.getId());
                c.setMatriculado(isMatriculado);
                if(!isMatriculado)
                    c.setAulas(null);
            }
            listCurso.add(c);
        }
        model.addAttribute("usuarioLogado", usuario);
        model.addAttribute("perfil", usuario.getPerfil());
        model.addAttribute("cursos", listCurso);
        return "curso/cursos";
    }

    @GetMapping("/curso/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        var curso = cursoRepository.findById(id);
        var cursoDto = CursoDTO.toDto(curso.orElseThrow(() ->
                new RuntimeException("Curso não encontrado.")));
        model.addAttribute("curso", cursoDto);
        return "curso/formcurso";
    }

    @GetMapping("/curso/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            if(!cursoService.possuiArquivo(id)){
                cursoService.excluirPorId(id);
            }else{
                cursoService.removerComArquivos(id);
            }
        } catch (RuntimeException e) {
            redirect.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/cursos";
    }
}
