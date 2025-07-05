package com.service;

import com.dto.MatriculaDTO;
import com.filtro.FiltroMatricula;
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

@Service
public class MatriculaService {

    @Autowired
    private MatriculaRepository matriculaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CursoRepository cursoRepository;

    public void salvarMatricula(Long idUsuario, Long idCurso){
        var dto = new MatriculaDTO();
        dto.setCursoId(idCurso);
        dto.setUsuarioId(idUsuario);
        dto.setDataMatricula(new Date());
        salvarMatricula(dto);
    }

    private void salvarMatricula(MatriculaDTO dto) {
        Matricula matricula = new Matricula();

        if (dto.getId() != null) {
            matricula = matriculaRepository.findById(dto.getId())
                    .orElseThrow(() -> new RuntimeException("Matrícula não encontrada"));
        }

        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Curso curso = cursoRepository.findById(dto.getCursoId())
                .orElseThrow(() -> new RuntimeException("Curso não encontrado"));

        matricula.setUsuario(usuario);
        matricula.setCurso(curso);

        if (dto.getId() == null) {
            matricula.setDataMatricula(new Date());
        }
        matriculaRepository.save(matricula);
    }

    public List<Matricula> buscarMatriculasPorAluno(Long idUsuario) {
        var filtro = new FiltroMatricula();
        filtro.setIdUsuario(idUsuario);
        return matriculaRepository.findAll(filtro.toSpecification());
    }

    public void excluirPorUsuarioCurso(Long idCurso, Long idUsuario){
        var filtro = new FiltroMatricula();
        filtro.setIdCurso(idCurso);
        filtro.setIdUsuario(idUsuario);

        List<Matricula> matriculas = matriculaRepository.findAll(filtro.toSpecification());

        for(Matricula mat : matriculas){
            matriculaRepository.deleteById(mat.getId());
        }
    }

    public Object buscarTodas() {
        return matriculaRepository.findAll();
    }

    public Object buscarMatriculasDoAluno(Usuario aluno) {
        return matriculaRepository.findAll();
    }
}
