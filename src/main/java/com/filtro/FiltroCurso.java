package com.filtro;

import com.model.Curso;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.ui.Model;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class FiltroCurso {
    private Long id;
    private String titulo;

    public Specification<Curso> toSpecification() {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<Predicate>();

            if(id != null && id > 0) {
                predicates.add(criteriaBuilder.equal(root.get("id"), id));
            }

            if(!StringUtils.isEmpty(titulo)) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.upper(root.get("titulo")), "%"+titulo.toUpperCase()+"%"));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public void preencheFiltro(Model model) {
        var titulo = model.getAttribute("titulo");
        if(titulo != null && !titulo.toString().isEmpty()) {
            this.titulo = titulo.toString();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}
