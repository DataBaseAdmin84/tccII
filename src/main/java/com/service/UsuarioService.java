package com.service;

import com.dto.LoginDTO;
import com.filtro.FiltroUsuario;
import com.model.Usuario;
import com.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CursoService cursoService;

    public boolean validarEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public boolean validarLogin(String login) {
        return usuarioRepository.findByLogin(login).isEmpty();
    }

    public boolean autenticar(LoginDTO login) {
        var filtro = new FiltroUsuario();
        filtro.setLogin(login.getLogin());
        filtro.setSenha(login.getSenha());
        var usuario = usuarioRepository.findAll(filtro.toSpecification());
        if (usuario.isEmpty()){
            return false;
        }
        return true;
    }

    public Usuario findUser(LoginDTO login){
        var filtro = new FiltroUsuario();
        filtro.setLogin(login.getLogin());
        filtro.setSenha(login.getSenha());
        var usuario = usuarioRepository.findAll(filtro.toSpecification());
        if (!usuario.isEmpty()){
            return usuario.getFirst();
        }
        return null;
    }

    public void removeVincululos(Usuario usuario){
        cursoService.removerCursosPorProfessor(usuario);
    }
}
