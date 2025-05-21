package com.service;

import com.model.Aula;
import com.model.Curso;

import com.model.Usuario;
import com.repository.AulaRepository;
import com.repository.CursoRepository;
import com.repository.UsuarioRepository;
import com.repository.AulaRepository;
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
        Curso curso = cursoRepository.findById(cursoId).orElseThrow(() -> new RuntimeException("Curso não encontrado"));
        return aulaRepository.findByCurso(curso);
    }

    public List<Aula> listarPorProfessor(Long professorId) {
        Usuario professor = usuarioRepository.findById(professorId).orElseThrow(() -> new RuntimeException("Professor não encontrado"));
        return aulaRepository.findByProfessor(professor);
    }

    public List<Aula> buscarPorTitulo(String titulo) {
        return aulaRepository.findByTituloContainingIgnoreCase(titulo);
    }

    public List<Aula> buscarPorDescricao(String descricao) {
        return aulaRepository.findByDescricaoContainingIgnoreCase(descricao);
    }

    public List<Aula> buscarPorUrlConteudo(String urlConteudo) {
        return aulaRepository.findByUrlConteudoContainingIgnoreCase(urlConteudo);
    }

    public List<Aula> buscarPorCursoETitulo(Long cursoId, String titulo) {
        Curso curso = cursoRepository.findById(cursoId).orElseThrow(() -> new RuntimeException("Curso não encontrado"));
        return aulaRepository.findByCursoAndTituloContainingIgnoreCase(curso, titulo);
    }

    public List<Aula> buscarPorCursoEDescricao(Long cursoId, String descricao) {
        Curso curso = cursoRepository.findById(cursoId).orElseThrow(() -> new RuntimeException("Curso não encontrado"));
        return aulaRepository.findByCursoAndDescricaoContainingIgnoreCase(curso, descricao);
    }

    public List<Aula> buscarPorCursoEUrlConteudo(Long cursoId, String urlConteudo) {
        Curso curso = cursoRepository.findById(cursoId).orElseThrow(() -> new RuntimeException("Curso não encontrado"));
        return aulaRepository.findByCursoAndUrlConteudoContainingIgnoreCase(curso, urlConteudo);
    }

    public Aula buscarPorId(Long id) {
        return aulaRepository.findById(id).orElse(null);
    }

    public List<Aula> listarPorCurso(Curso curso) {
        return aulaRepository.findByCurso(curso);
    }
}
