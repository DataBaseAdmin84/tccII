package com.dto;

import com.model.Usuario;

public class UsuarioDTO {

    private Long id;
    private String nomeCompleto;
    private String email;
    private String login;
    private String senha;
    private Integer perfil;

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

    public Integer getPerfil() {
        return perfil;
    }

    public void setPerfil(Integer perfil) {
        this.perfil = perfil;
    }


    public boolean isPresent() {
        return id != null && id > 0;
    }

    public Usuario create() {
        Usuario usuario = new Usuario();
        usuario.setId(this.id);
        usuario.setNomeCompleto(this.nomeCompleto);
        usuario.setEmail(this.email);
        usuario.setLogin(this.login);
        usuario.setSenha(this.senha);
        usuario.setPerfil(this.perfil);
        return usuario;
    }
}
