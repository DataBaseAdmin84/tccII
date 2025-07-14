package com.dto;

import com.model.Arquivo;

public class ArquivoDTO {

    private Long id;
    private String nome;
    private String caminho;
    private String tipo;
    private Long tamanho;

    public ArquivoDTO() {}

    public static ArquivoDTO toDto(Arquivo arquivo) {
        if (arquivo == null) {
            return null;
        }
        ArquivoDTO dto = new ArquivoDTO();
        dto.setId(arquivo.getId());
        dto.setNome(arquivo.getNome());
        dto.setCaminho(arquivo.getCaminho());
        dto.setTipo(arquivo.getTipo());
        dto.setTamanho(arquivo.getTamanho());
        return dto;
    }

    public static Arquivo toModel(ArquivoDTO arquivoDTO) {
        if (arquivoDTO == null) {
            return null;
        }
        Arquivo model = new Arquivo();
        model.setId(arquivoDTO.getId());
        model.setNome(arquivoDTO.getNome());
        model.setCaminho(arquivoDTO.getCaminho());
        model.setTipo(arquivoDTO.getTipo());
        model.setTamanho(arquivoDTO.getTamanho());
        return model;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCaminho() {
        return caminho;
    }

    public void setCaminho(String caminho) {
        this.caminho = caminho;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Long getTamanho() {
        return tamanho;
    }

    public void setTamanho(Long tamanho) {
        this.tamanho = tamanho;
    }
}