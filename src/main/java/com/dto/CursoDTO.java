package com.dto;

import com.dataUtil.DataUtils;
import com.model.Curso;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CursoDTO {

    private Long id;
    private String descricao;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate data;
    private String titulo;
    private String professorNome;
    private String erro;
    private List<AulaDTO> aulas = new ArrayList<>();

    public String getProfessorNome() {
        return professorNome;
    }

    public void setProfessorNome(String professorNome) {
        this.professorNome = professorNome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getErro() {
        return erro;
    }

    public void setErro(String erro) {
        this.erro = erro;
    }

    public List<AulaDTO> getAulas() {
        return aulas;
    }

    public void setAulas(List<AulaDTO> aulas) {
        this.aulas = aulas;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public static CursoDTO toDto(Curso curso){
        var dto = new CursoDTO();
        dto.setId(curso.getId());
        dto.setTitulo(curso.getTitulo());
        dto.setDescricao(curso.getDescricao());
        dto.setData(DataUtils.dateToLocalDate(curso.getData()));
        if(curso.getProfessor() != null) {
            dto.setProfessorNome(curso.getProfessor().getNomeCompleto());
        }
        if(curso.getAulas() != null && !curso.getAulas().isEmpty()) {
            //implementar preenchimento de aulas.
        }
        return dto;
    }
}
