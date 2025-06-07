package com.service;

import com.dto.CursoDTO;
import com.model.Curso;
import com.model.Usuario;
import com.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    public void salvarCurso(CursoDTO dto, Usuario professor) {
        Curso curso = (dto.getId() != null) ?
                cursoRepository.findById(dto.getId()).orElse(new Curso()) : new Curso();

        curso.setNome(dto.getNome());
//        curso.setUrlVideo(dto.getUrlVideo());
//        curso.setUrlPdf(dto.getUrlPdf());
        curso.setProfessor(professor);

        curso = cursoRepository.save(curso);
        dto.setId(curso.getId());
    }

    public List<CursoDTO> listarCursos() {
        return cursoRepository.findAll().stream().map(curso -> {
            CursoDTO dto = new CursoDTO();
            dto.setId(curso.getId());
            dto.setNome(curso.getNome());
            dto.setProfessorId(curso.getProfessor().getId());
//            dto.setProfessorNome(curso.getProfessor().getNomeCompleto());
            return dto;
        }).collect(Collectors.toList());
    }

    public CursoDTO buscarPorId(Long id) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso não encontrado"));

        CursoDTO dto = new CursoDTO();
        dto.setId(curso.getId());
        dto.setNome(curso.getNome());
//        dto.setUrlVideo(curso.getUrlVideo());
//        dto.setUrlPdf(curso.getUrlPdf());
        dto.setProfessorId(curso.getProfessor().getId());
        return dto;
    }

    public List<Curso> listarTodos() {
        return cursoRepository.findAll();
    }

    public List<CursoDTO> listarCursosPorProfessor(Usuario professor) {
        return cursoRepository.findByProfessor(professor).stream().map(curso -> {
            CursoDTO dto = new CursoDTO();
            dto.setId(curso.getId());
            dto.setNome(curso.getNome());
            dto.setProfessorId(professor.getId());
//            dto.setProfessorNome(professor.getNomeCompleto());

//            dto.setUrlVideo(curso.getUrlVideo());
//            dto.setUrlPdf(curso.getUrlPdf());
            return dto;
        }).collect(Collectors.toList());
    }

    public void excluirPorId(Long id) {
        try {
            cursoRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Não é possível excluir este curso. Existem matrículas vinculadas a ele.");
        }
    }

    public void excluirCursoSePertencerAoProfessor(Long id, Usuario professor) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso não encontrado"));

        if (!curso.getProfessor().getId().equals(professor.getId())) {
            throw new RuntimeException("Curso não pertence ao professor logado");
        }

        cursoRepository.deleteById(id);
    }
}
