package com.trainshier.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "errores_simulacion")
@Data
public class SimulationError {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "descripcion")
    private String description;

    @Column(name = "ocurrido_en")
    private LocalDateTime occurredAt;

    @ManyToOne
    @JoinColumn(name = "transaccion_id")
    private Transaction transaction;

    @ManyToOne
    @JoinColumn(name = "catalogo_error_id")
    private ErrorCatalog errorCatalog;
}