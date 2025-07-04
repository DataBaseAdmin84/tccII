package com.service;

import com.dataUtil.DataUtils;
import com.dto.CursoDTO;
import com.model.Curso;
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

    public void salvarCurso(CursoDTO dto, Usuario professor) {
        Curso curso = (dto.getId() != null) ?
                cursoRepository.findById(dto.getId()).orElse(new Curso()) : new Curso();

        //curso.setNome(dto.getNome());
//       curso.setUrlVideo(dto.getUrlVideo());
//        curso.setUrlPdf(dto.getUrlPdf());
//        curso.setProfessor(professor);

        curso = cursoRepository.save(curso);
        dto.setId(curso.getId());
    }

    public CursoDTO buscarPorId(Long id) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso não encontrado"));
        return CursoDTO.toDto(curso);
    }

    public List<Curso> listarTodos() {
        return cursoRepository.findAll();
    }


    public void excluirPorId(Long id) {
        try {
            cursoRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Não é possível excluir este curso. Existem matrículas vinculadas a ele.");
        }
    }
}
