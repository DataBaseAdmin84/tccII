package com.filtro;

import com.dto.CursoDTO;
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
    private String descricao;
    private Long idProfessor;
    private String nomeProfessor;

    public Specification<Curso> toSpecification() {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<Predicate>();

            if(id != null && id > 0) {
                predicates.add(criteriaBuilder.equal(root.get("id"), id));
            }

            if(!StringUtils.isEmpty(titulo)) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.upper(root.get("titulo")), "%"+titulo.toUpperCase()+"%"));
            }

            if(idProfessor != null) {
                predicates.add(criteriaBuilder.equal(root.join("professor").get("id"), idProfessor));
            }

            if(!StringUtils.isEmpty(nomeProfessor)) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.upper(root.join("professor").get("nomeCompleto")), "%" + nomeProfessor.toUpperCase() + "%"));
            }

            if(!StringUtils.isEmpty(descricao)) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.upper(root.get("descricao")), "%"+descricao.toUpperCase()+"%"));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public void preencheFiltro(CursoDTO dto) {
        if(dto.getId() != null){
            setId(dto.getId());
        }
        if(dto.getTitulo() != null && !dto.getTitulo().isEmpty()){
            setTitulo(dto.getTitulo());
        }
        if(dto.getProfessorNome() != null && !dto.getProfessorNome().isEmpty()){
            setNomeProfessor(dto.getProfessorNome());
        }
        if(dto.getDescricao() != null && !dto.getDescricao().isEmpty()){
            setDescricao(dto.getDescricao());
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getNomeProfessor() {
        return nomeProfessor;
    }

    public void setNomeProfessor(String nomeProfessor) {
        this.nomeProfessor = nomeProfessor;
    }

    public Long getIdProfessor() {
        return idProfessor;
    }

    public void setIdProfessor(Long idProfessor) {
        this.idProfessor = idProfessor;
    }
}
