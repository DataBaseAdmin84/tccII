package com.filtro;

import com.model.ArquivoAula;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class FiltroArquivoAula {
    private Long id;
    private Long idAula;
    private Long idArquivo;

    public Specification<ArquivoAula> toSpecification() {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<Predicate>();

            if(id != null) {
                predicates.add(criteriaBuilder.equal(root.get("id"), id));
            }

            if (idAula != null) {
                predicates.add(criteriaBuilder.equal(root.get("aula").get("id"), idAula));
            }

            if (idArquivo != null) {
                predicates.add(criteriaBuilder.equal(root.get("arquivo").get("id"), idArquivo));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public Long getIdAula() {return idAula;}
    public void setIdAula(Long idAula) {this.idAula = idAula;}

    public Long getIdArquivo() {return idArquivo;}
    public void setIdArquivo(Long idArquivo) {this.idArquivo = idArquivo;}
}
