package com.service;

import com.dto.UsuarioDTO;
import com.model.Usuario;
import com.repository.CursoRepository;
import com.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CursoService cursoService;

    public boolean validarEmail(String email, String senha) {
        return usuarioRepository.existsByEmailAndSenha(email, senha);
    }

    public Optional<Usuario> autenticar(String login, String senha) {
        return usuarioRepository.findByLoginAndSenha(login, senha);
    }

    public void removeVincululos(Usuario usuario){
        cursoService.removerCursosPorProfessor(usuario);
    }
}
