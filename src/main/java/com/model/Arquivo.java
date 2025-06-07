package com.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Arquivo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String nome;

    @Column(nullable = false, length = 200)
    private String caminho;

    @Column(nullable = false, length = 20)
    private String tipo;

    @Column(nullable = false)
    private Long tamanho;

    @Column(nullable = false)
    private LocalDate dataUpload;

}
