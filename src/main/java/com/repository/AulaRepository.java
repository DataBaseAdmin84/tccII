package com.repository;

import com.model.Aula;
import com.model.Curso;

import com.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AulaRepository extends JpaRepository<Aula, Long> {
    List<Aula> findByCurso(Curso curso);

    List<Aula> findByProfessor(Usuario professor);
    List<Aula> findByCursoAndProfessor(Curso curso, Usuario professor);
    List<Aula> findByTituloContainingIgnoreCase(String titulo);
    List<Aula> findByDescricaoContainingIgnoreCase(String descricao);
    List<Aula> findByUrlConteudoContainingIgnoreCase(String urlConteudo);
    List<Aula> findByCursoAndTituloContainingIgnoreCase(Curso curso, String titulo);
    List<Aula> findByCursoAndDescricaoContainingIgnoreCase(Curso curso, String descricao);

    List<Aula> findByCursoAndUrlConteudoContainingIgnoreCase(Curso curso, String urlConteudo);


}
