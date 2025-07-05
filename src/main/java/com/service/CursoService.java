package com.service;

import com.dataUtil.DataUtils;
import com.dto.CursoDTO;
import com.model.Curso;
import com.model.Matricula;
import com.model.Usuario;
import com.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private MatriculaService matriculaService;

    public CursoDTO buscarPorId(Long id) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso não encontrado"));
        return CursoDTO.toDto(curso);
    }

    public void removerCursosPorProfessor(Usuario usuario) {
        List<Curso> cursos = cursoRepository.findByProfessor(usuario);
        cursoRepository.deleteAll(cursos);
    }

    public void excluirPorId(Long id) {
        try {
            cursoRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Não é possível excluir este curso. Existem matrículas vinculadas a ele.");
        }
    }

    public boolean possuiMatricula(Curso curso, Long idAluno){
        var matriculas = matriculaService.buscarMatriculasPorAluno(idAluno);
        for(Matricula matricula : matriculas){
            if(matricula.getCurso().getId().equals(curso.getId()))
                return true;
        }
        return false;
    }
}
