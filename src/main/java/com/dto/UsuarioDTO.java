package com.dto;

import com.model.Perfil;
import com.model.Usuario;

public class UsuarioDTO {

    private Long id;
    private String nome;
    private String nomeCompleto;
    private String email;
    private String login;
    private String senha;
    private Perfil perfil;
    private String tipoUsuario;

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

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public boolean isPresent() {
        return id != null && id > 0;
    }

    public Usuario get() {
        Usuario usuario = new Usuario();
        usuario.setId(this.id);
        usuario.setNomeCompleto(this.nomeCompleto);
        usuario.setEmail(this.email);
        usuario.setLogin(this.login);
        usuario.setSenha(this.senha);
        usuario.setPerfil(this.perfil);
        usuario.setTipoUsuario(this.tipoUsuario);
        return usuario;
    }
}
