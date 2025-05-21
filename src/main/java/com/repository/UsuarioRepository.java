package com.repository;

import com.model.Perfil;
import com.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByLoginAndSenha(String login, String senha);

    boolean existsByLogin(String login);

    boolean existsByEmail(String email);

    List<Usuario> findByPerfil(Perfil perfil);
    List<Usuario> findByNomeCompletoContainingIgnoreCase(String nomeCompleto);

}
