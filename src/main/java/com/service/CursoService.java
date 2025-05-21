package com.service;

import com.dto.CursoDTO;
import com.model.Curso;
import com.model.Usuario;
import com.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    public CursoDTO salvarCurso(CursoDTO dto, Usuario professor) {
        Curso curso = (dto.getId() != null) ?
                cursoRepository.findById(dto.getId()).orElse(new Curso()) : new Curso();

        curso.setNome(dto.getNome());
        curso.setDescricao(dto.getDescricao());
        curso.setProfessor(professor);

        curso = cursoRepository.save(curso);
        dto.setId(curso.getId());
        return dto;
    }

    public List<CursoDTO> listarCursos() {
        return cursoRepository.findAll().stream().map(curso -> {
            CursoDTO dto = new CursoDTO();
            dto.setId(curso.getId());
            dto.setNome(curso.getNome());
            dto.setDescricao(curso.getDescricao());
            dto.setProfessorId(curso.getProfessor().getId());
            dto.setProfessorNome(curso.getProfessor().getNomeCompleto());
            return dto;
        }).collect(Collectors.toList());
    }

    public CursoDTO buscarPorId(Long id) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso não encontrado"));
        CursoDTO dto = new CursoDTO();
        dto.setId(curso.getId());
        dto.setNome(curso.getNome());
        dto.setDescricao(curso.getDescricao());
        return dto;
    }

    public void excluirCurso(Long id) {
        cursoRepository.deleteById(id);
    }

    public List<Curso> listarTodos() {
        return cursoRepository.findAll();
    }

    public List<CursoDTO> listarCursosPorProfessor(Usuario professor) {
        return cursoRepository.findByProfessor(professor).stream().map(curso -> {
            CursoDTO dto = new CursoDTO();
            dto.setId(curso.getId());
            dto.setNome(curso.getNome());
            dto.setDescricao(curso.getDescricao());
            dto.setProfessorId(professor.getId());
            dto.setProfessorNome(professor.getNomeCompleto());
            return dto;
        }).collect(Collectors.toList());
    }
}
