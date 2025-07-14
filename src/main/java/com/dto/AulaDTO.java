package com.dto;

import com.model.Aula;

import java.util.List;
import java.util.stream.Collectors;

public class AulaDTO {
    private Long id;
    private String titulo;
    private String descricao;
    private Long idCurso;
    private List<ArquivoDTO> arquivos;

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Long getIdCurso() {return idCurso; }
    public void setIdCurso(Long idCurso) {this.idCurso = idCurso;}

    public List<ArquivoDTO> getArquivos() {
        return arquivos;
    }

    public void setArquivos(List<ArquivoDTO> arquivos) {
        this.arquivos = arquivos;
    }

    public static AulaDTO toDto(Aula aula){
        AulaDTO aulaDTO = new AulaDTO();
        aulaDTO.setId(aula.getId());
        aulaDTO.setDescricao(aula.getDescricao());
        aulaDTO.setIdCurso(aula.getCurso().getId());
        if (aula.getArquivos() != null && !aula.getArquivos().isEmpty()) {
            aulaDTO.setArquivos(
                    aula.getArquivos().stream()
                            .map(ArquivoDTO::toDto)
                            .collect(Collectors.toList())
            );
        }

        return aulaDTO;
    }

    public static Aula toModel(AulaDTO aulaDTO){
        Aula aula = new Aula();
        aula.setId(aulaDTO.getId());
        aula.setDescricao(aulaDTO.getDescricao());
        return aula;
    }
}
