package com.repository;
import java.util.List;
import com.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    List<Usuario> findByLogin(String login);
    boolean existsByEmail(String email);

    boolean existsByEmailAndLogin(String email, String login);
}
