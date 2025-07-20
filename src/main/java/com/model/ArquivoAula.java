package com.model;

import jakarta.persistence.*;

@Entity
@Table(name = "AULA_ARQUIVO")
public class ArquivoAula {
    @Id
    @Column(name = "ID")
    @SequenceGenerator(name = "HIBERNATE_SEQUENCE", sequenceName = "HIBERNATE_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HIBERNATE_SEQUENCE")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ID_AULA")
    private Aula aula;

    @ManyToOne
    @JoinColumn(name = "ID_ARQUIVO")
    private Arquivo arquivo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Aula getAula() {
        return aula;
    }

    public void setAula(Aula aula) {
        this.aula = aula;
    }

    public Arquivo getArquivo() {
        return arquivo;
    }

    public void setArquivo(Arquivo arquivo) {
        this.arquivo = arquivo;
    }
}
