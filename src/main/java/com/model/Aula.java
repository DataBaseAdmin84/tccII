package com.model;

import jakarta.persistence.*;

@Entity
@Table(name = "AULA")
public class Aula {

    @Id
    @Column(name = "ID")
    @SequenceGenerator(name = "HIBERNATE_SEQUENCE", sequenceName = "HIBERNATE_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HIBERNATE_SEQUENCE")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ID_CURSO")
    private Curso curso;

    @Column(name = "DESCRICAO", nullable = false)
    private String descricao;

    //TODO ADICIONAR ARQUIVOS
    //List arquivos


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }
}
