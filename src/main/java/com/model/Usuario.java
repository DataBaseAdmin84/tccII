package com.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

@Entity
@Table(name = "USUARIO")
public class Usuario {

    @Id
    @Column(name = "ID")
    @SequenceGenerator(name = "HIBERNATE_SEQUENCE", sequenceName = "HIBERNATE_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HIBERNATE_SEQUENCE")
    private Long id;

    @NotNull
    @Column(name = "NOME_COMPLETO", length = 200)
    private String nomeCompleto;

    @NotNull
    @Column(name = "EMAIL", length = 200)
    private String email;

    @NotNull
    @Column(name = "LOGIN", length = 200)
    private String login;

    @NotNull
    @Column(name = "SENHA", length = 150)
    private String senha;

    @NotNull
    @Column(name = "DATA_INCLUSAO")
    private Date dataInclusao;

    @NotNull
    @Column(name = "PERFIL")
    private Integer perfil;

    @Transient
    private String inicial;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Date getDataInclusao() {
        return dataInclusao;
    }

    public void setDataInclusao(Date dataInclusao) {
        this.dataInclusao = dataInclusao;
    }

    public Integer getPerfil() {
        return perfil;
    }
    public void setPerfil(Integer perfil) {
        this.perfil = perfil;
    }

    public String getInicial() {
        if(!nomeCompleto.isEmpty())
            return nomeCompleto.substring(0, 2).toUpperCase();
        else return "";
    }

    public void setInicial(String inicial) {
        this.inicial = inicial;
    }
}

