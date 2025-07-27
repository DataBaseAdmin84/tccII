package com.filtro;

import com.model.Usuario;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class FiltroUsuario {
    private String senha;
    private String login;


    public Specification<Usuario> toSpecification() {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<Predicate>();

            if(senha != null && !senha.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("senha"), senha));
            }
            if(login != null && !login.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("login"), login));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
