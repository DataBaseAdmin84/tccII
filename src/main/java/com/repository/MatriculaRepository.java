package com.repository;

import com.model.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatriculaRepository extends JpaRepository<Matricula, Long> {
    List<Matricula> findByUsuarioId(Long usuarioId);

    boolean existsByUsuarioIdAndCursoId(Long usuarioId, Long cursoId);
}