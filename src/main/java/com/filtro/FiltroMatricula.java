package com.filtro;

import com.model.Matricula;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class FiltroMatricula {
    private Long id;
    private Long idCurso;
    private Long idUsuario;

    public Specification<Matricula> toSpecification() {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<Predicate>();

            if(id != null) {
                predicates.add(criteriaBuilder.equal(root.get("id"), id));
            }
            if(idCurso != null){
                predicates.add(criteriaBuilder.equal(root.join("curso").get("id"), idCurso));
            }
            if(idUsuario != null){
                predicates.add(criteriaBuilder.equal(root.join("usuario").get("id"), idUsuario));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(Long idCurso) {
        this.idCurso = idCurso;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }
}
