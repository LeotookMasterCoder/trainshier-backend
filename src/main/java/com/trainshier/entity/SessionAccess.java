package com.trainshier.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "accesos_sesion")
@Data
public class SessionAccess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_acceso")
    private Long id;

    @Column(name = "ingreso_en")
    private LocalDateTime loginAt;

    @Column(name = "salida_en")
    private LocalDateTime logoutAt;

    @ManyToOne
    @JoinColumn(name = "sesion_id", nullable = false)
    private SimulationSession session;

    @ManyToOne
    @JoinColumn(name = "aprendiz_id", nullable = false)
    private User apprentice;
}