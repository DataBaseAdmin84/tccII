package com.service;

import com.dto.UsuarioDTO;
import com.model.Usuario;
import com.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Salvar um novo usuário
    public UsuarioDTO salvarUsuario(UsuarioDTO dto) {
        if (dto.getId() == null) {
            if (usuarioRepository.existsByLogin(dto.getLogin())) {
                throw new RuntimeException("Já existe um usuário com este login.");
            }
            if (usuarioRepository.existsByEmail(dto.getEmail())) {
                throw new RuntimeException("Já existe um usuário com este e-mail.");
            }
        }

        Usuario usuario = new Usuario();

        if (dto.getId() != null) {
            usuario = usuarioRepository.findById(dto.getId()).orElse(new Usuario());
        }

        usuario.setNomeCompleto(dto.getNomeCompleto());
        usuario.setEmail(dto.getEmail());
        usuario.setLogin(dto.getLogin());
        usuario.setSenha(dto.getSenha());
        usuario.setDataInclusao(new Date());

        Usuario salvo = usuarioRepository.save(usuario);
        dto.setId(salvo.getId());

        return dto;
    }

    // Listar todos usuários como DTO (para telas tipo listar usuários)
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

    // Listar todos usuários como entidades (para forms tipo matrícula)
    public List<Usuario> listarEntidades() {
        return usuarioRepository.findAll();
    }

    // Buscar usuário por ID
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

    // Excluir usuário
    public void excluirUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }

    // Autenticação simples
    public Optional<Usuario> autenticar(String login, String senha) {
        return usuarioRepository.findByLoginAndSenha(login, senha);
    }

    // Excluir por ID
    public void excluirPorId(Long id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if (usuario.isPresent()) {
            usuarioRepository.delete(usuario.get());
        } else {
            throw new RuntimeException("Usuário não encontrado");
        }
    }

    // Salvar usuário (sem DTO)
    public void salvar(Usuario usuario) {
        if (usuario.getId() == null) {
            if (usuarioRepository.existsByLogin(usuario.getLogin())) {
                throw new RuntimeException("Já existe um usuário com este login.");
            }
            if (usuarioRepository.existsByEmail(usuario.getEmail())) {
                throw new RuntimeException("Já existe um usuário com este e-mail.");
            }
        }
        usuario.setDataInclusao(new Date());
        usuarioRepository.save(usuario);
    }
    public List<Usuario> listarPorPerfil(com.model.Perfil perfil) {
        return usuarioRepository.findByPerfil(perfil);
    }

    public Usuario buscarEntidadePorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public Object buscarTodos() {
        return usuarioRepository.findAll();
    }
}
