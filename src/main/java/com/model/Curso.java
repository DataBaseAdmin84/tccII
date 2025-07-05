package com.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Curso {

    @Id
    @Column(name = "ID")
    @SequenceGenerator(name = "HIBERNATE_SEQUENCE", sequenceName = "HIBERNATE_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HIBERNATE_SEQUENCE")
    private Long id;

    @NotNull
    @Column(name = "TITULO", length = 200)
    private String titulo;

    @NotNull
    @Column(name = "DESCRICAO", length = 500)
    private String descricao;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "ID_PROFESSOR")
    private Usuario professor;

    @NotNull
    @Column(name = "DATA")
    private Date data;

    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL)
    private List<Aula> aulas = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "MATRICULA",
            joinColumns = @JoinColumn(name = "ID_CURSO"),
            inverseJoinColumns = @JoinColumn(name = "ID_ALUNO")
    )
    private List<Usuario> alunos = new ArrayList<>();

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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Usuario getProfessor() {
        return professor;
    }

    public void setProfessor(Usuario professor) {
        this.professor = professor;
    }

    public List<Aula> getAulas() {
        return aulas;
    }

    public void setAulas(List<Aula> aulas) {
        this.aulas = aulas;
    }

    public List<Usuario> getAlunos() {
        return alunos;
    }

    public void setAlunos(List<Usuario> alunos) {
        this.alunos = alunos;
    }
}
