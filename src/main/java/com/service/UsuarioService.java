package com.service;

import com.dto.UsuarioDTO;
import com.model.Usuario;
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

    public boolean validar(String dto) {
        var x = usuarioRepository.existsByEmail(dto);
        return usuarioRepository.existsByEmail(dto);
    }


    public List<UsuarioDTO> listarTodos() {
        return usuarioRepository.findAll().stream().map(usuario -> {
            UsuarioDTO dto = new UsuarioDTO();
            dto.setId(usuario.getId());
            dto.setNomeCompleto(usuario.getNomeCompleto());
            dto.setEmail(usuario.getEmail());
            dto.setLogin(usuario.getLogin());
            dto.setSenha(usuario.getSenha());
            return dto;
        }).collect(Collectors.toList());
    }


    public List<Usuario> listarEntidades() {
        return usuarioRepository.findAll();
    }


    public UsuarioDTO buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNomeCompleto(usuario.getNomeCompleto());
        dto.setEmail(usuario.getEmail());
        dto.setLogin(usuario.getLogin());
        dto.setSenha(usuario.getSenha());

        return dto;
    }


    public void excluirUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }


    public Optional<Usuario> autenticar(String login, String senha) {
        return usuarioRepository.findByLoginAndSenha(login, senha);
    }


    public void excluirPorId(Long id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if (usuario.isPresent()) {
            usuarioRepository.delete(usuario.get());
        } else {
            throw new RuntimeException("Usuário não encontrado");
        }
    }

    public Usuario buscarEntidadePorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public Object buscarTodos() {
        return usuarioRepository.findAll();
    }
}
