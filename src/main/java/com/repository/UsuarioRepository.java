package com.repository;
import com.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>, JpaSpecificationExecutor<Usuario> {
    List<Usuario> findByLogin(String login);
    boolean existsByEmail(String email);
}
