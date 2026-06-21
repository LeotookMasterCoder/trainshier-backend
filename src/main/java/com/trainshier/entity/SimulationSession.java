package com.trainshier.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "sesiones_simulacion")
@Data
public class SimulationSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sesion")
    private Long id;

    @Column(name = "codigo_acceso", unique = true, nullable = false)
    private String accessCode;

    @Column(name = "activa")
    private Boolean active;

    @Column(name = "creada_en")
    private LocalDateTime createdAt;

    @Column(name = "expira_en")
    private LocalDateTime expiresAt;

    @ManyToOne
    @JoinColumn(name = "instructor_id", nullable = false)
    private User instructor;

    @ManyToOne
    @JoinColumn(name = "practica_id")
    private Practice practice;
}