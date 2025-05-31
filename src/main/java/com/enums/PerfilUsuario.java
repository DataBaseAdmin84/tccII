package com.enums;

public enum PerfilUsuario {
    ALUNO(1, "Aluno"),
    PROFESSOR(2, "Professor");

    private int codigo;
    private String descricao;

    PerfilUsuario(int codigo, String descricao) {

        this.codigo = codigo;
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }
}
