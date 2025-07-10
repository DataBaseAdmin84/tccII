package com.dto;

import com.model.Aula;

public class AulaDTO {
    private Long id;
    private String titulo;
    private String descricao;
    private Long idCurso;

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Long getIdCurso() {return idCurso; }
    public void setIdCurso(Long idCurso) {this.idCurso = idCurso;}

    public static AulaDTO toDto(Aula aula){
        AulaDTO aulaDTO = new AulaDTO();
        aulaDTO.setId(aula.getId());
        aulaDTO.setDescricao(aula.getDescricao());
        aulaDTO.setIdCurso(aula.getCurso().getId());

        return aulaDTO;
    }

    public static Aula toModel(AulaDTO aulaDTO){
        Aula aula = new Aula();
        aula.setId(aulaDTO.getId());
        aula.setDescricao(aulaDTO.getDescricao());
        return aula;
    }
}
