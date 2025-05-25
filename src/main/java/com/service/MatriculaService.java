package com.service;

import com.dto.MatriculaDTO;
import com.model.Curso;
import com.model.Matricula;
import com.model.Usuario;
import com.repository.CursoRepository;
import com.repository.MatriculaRepository;
import com.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class MatriculaService {

    @Autowired
    private MatriculaRepository matriculaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CursoRepository cursoRepository;

    // Lista todas as matrículas
    public List<Matricula> listarMatriculas() {
        return matriculaRepository.findAll();
    }

    // Salva nova matrícula ou atualiza matrícula existente
    public void salvarMatricula(MatriculaDTO dto) {
        Matricula matricula = new Matricula();

        if (dto.getId() != null) { // Se tem ID, é edição
            matricula = matriculaRepository.findById(dto.getId())
                    .orElseThrow(() -> new RuntimeException("Matrícula não encontrada"));
        }

        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Curso curso = cursoRepository.findById(dto.getCursoId())
                .orElseThrow(() -> new RuntimeException("Curso não encontrado"));

        matricula.setUsuario(usuario);
        matricula.setCurso(curso);

        // Apenas no caso de novo cadastro definir a data atual
        if (dto.getId() == null) {
            matricula.setDataMatricula(new Date());
        }

        matriculaRepository.save(matricula);
    }

    // Buscar matrícula por ID (para edição)
    public Matricula buscarPorId(Long id) {
        return matriculaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Matrícula não encontrada"));
    }

    // Excluir matrícula
    public void excluirMatricula(Long id) {
        matriculaRepository.deleteById(id);
    }


    public void matricularAlunoEmCurso(Long alunoId, Long cursoId) {
        Optional<Usuario> alunoOpt = usuarioRepository.findById(alunoId);
        Optional<Curso> cursoOpt = cursoRepository.findById(cursoId);

        if (alunoOpt.isPresent() && cursoOpt.isPresent()) {
            Matricula matricula = new Matricula();
            matricula.setUsuario(alunoOpt.get());
            matricula.setCurso(cursoOpt.get());
            matricula.setDataMatricula(new Date());

            matriculaRepository.save(matricula);
        } else {
            throw new RuntimeException("Aluno ou curso não encontrado");
        }
    }

    public List<Matricula> buscarMatriculasPorAluno(Long alunoId) {
        return matriculaRepository.findByUsuarioId(alunoId);
    }


    public Object buscarTodas() {
        return matriculaRepository.findAll();
    }

    public Object buscarMatriculasDoAluno(Usuario aluno) {
        return matriculaRepository.findAll();
    }
}
