package com.service;

import com.model.Aula;
import com.model.Curso;
import com.model.Usuario;
import com.repository.AulaRepository;
import com.repository.CursoRepository;
import com.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AulaService {

    private final AulaRepository aulaRepository;
    private final CursoRepository cursoRepository;
    private final UsuarioRepository usuarioRepository;

    public AulaService(AulaRepository aulaRepository, CursoRepository cursoRepository, UsuarioRepository usuarioRepository) {
        this.aulaRepository = aulaRepository;
        this.cursoRepository = cursoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public Aula salvar(Aula aula) {
        return aulaRepository.save(aula);
    }

    public void atualizar(Aula aula) {
        aulaRepository.save(aula);
    }

    public void deletar(Long id) {
        aulaRepository.deleteById(id);
    }

    public List<Aula> listarPorCurso(Long cursoId) {
        return aulaRepository.findByCursoIdOrderByIdAsc(cursoId);
    }

    public List<Aula> listarPorProfessor(Long professorId) {
        Usuario professor = usuarioRepository.findById(professorId).orElseThrow(() -> new RuntimeException("Professor não encontrado"));
        return aulaRepository.findByProfessor(professor);
    }

    public Aula buscarPorId(Long id) {
        return aulaRepository.findById(id).orElse(null);
    }
}
