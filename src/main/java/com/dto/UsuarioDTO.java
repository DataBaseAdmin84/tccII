package com.dto;

import com.dataUtil.DataUtils;
import com.model.Curso;
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


    public static Usuario toModel(UsuarioDTO usuarioDto){
        var usuario = new Usuario();
        usuario.setId(usuarioDto.getId());
        usuario.setEmail(usuarioDto.getEmail());
        usuario.setLogin(usuarioDto.getLogin());
        usuario.setSenha(usuarioDto.getSenha());
        usuario.setPerfil(usuarioDto.getPerfil());
        usuario.setNomeCompleto(usuarioDto.getNomeCompleto());
        return usuario;
    }

    public static UsuarioDTO toDto(Usuario usuario){
        var dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setEmail(usuario.getEmail());
        dto.setLogin(usuario.getLogin());
        dto.setSenha(usuario.getSenha());
        dto.setPerfil(usuario.getPerfil());
        dto.setNomeCompleto(usuario.getNomeCompleto());
        return dto;
    }
}
