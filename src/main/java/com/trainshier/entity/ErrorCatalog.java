package com.trainshier.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "catalogo_errores")
@Data
public class ErrorCatalog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "codigo", unique = true, nullable = false)
    private String code;

    @Column(name = "descripcion", nullable = false)
    private String description;

    @Column(name = "solucion_sugerida")
    private String suggestedSolution;

    @Column(name = "categoria")
    private String category;
}