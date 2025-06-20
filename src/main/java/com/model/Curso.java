package com.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Curso {

    @Id
    @Column(name = "ID")
    @SequenceGenerator(name = "HIBERNATE_SEQUENCE", sequenceName = "HIBERNATE_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HIBERNATE_SEQUENCE")
    private Long id;

    @Column(nullable = false, length = 200)
    private String nome;

    public Long getId() {
        return id;
    }

    @Column(length = 500)
    private String descricao;

    @Column(length = 500)
    private String urlImagem;

    @Column
    private String lista;

    @OneToMany
    private  List<Matricula> matriculas;

    public String getLista() {
        return lista;
    }

    public List<Matricula> getMatriculas() {
        return matriculas;
    }

    public void setMatriculas(List<Matricula> matriculas) {
        this.matriculas = matriculas;
    }

    public void setLista(String lista) {
        this.lista = lista;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
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

    public String getDescricao() {
        return descricao;
    }

    public String getUrlImagem() {
        return urlImagem;
    }

}
