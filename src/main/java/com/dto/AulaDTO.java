package com.dto;

public class AulaDTO {
    private Long id;
    private String titulo;
    private String descricao;
    private String urlConteudo;
    private Long cursoId;
    private Long professorId;

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getUrlConteudo() { return urlConteudo; }
    public void setUrlConteudo(String urlConteudo) { this.urlConteudo = urlConteudo; }

    public Long getCursoId() { return cursoId; }
    public void setCursoId(Long cursoId) { this.cursoId = cursoId; }

    public Long getProfessorId() { return professorId; }
    public void setProfessorId(Long professorId) { this.professorId = professorId; }
}
