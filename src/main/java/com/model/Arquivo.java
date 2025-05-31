package com.model;

import java.time.LocalDate;

public class Arquivo {
    private String nome;
    private String caminho;
    private String tipo;
    private Long tamanho;
    private LocalDate dataUpload;

    public Arquivo() {
    }

    public Arquivo(String nome, String caminho, String tipo, Long tamanho) {
        this.nome = nome;
        this.caminho = caminho;
        this.tipo = tipo;
        this.tamanho = tamanho;
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

    public long getTamanho() {
        return tamanho;
    }

    public void setTamanho(Long tamanho) {
        this.tamanho = tamanho;
    }
    public LocalDate getDataUpload() {
        return dataUpload;
    }
    public void setDataUpload(LocalDate dataUpload) {
        this.dataUpload = dataUpload;
    }
}
