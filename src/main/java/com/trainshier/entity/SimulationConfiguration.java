package com.trainshier.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * @param id configuration identifier
 * @param session associated session
 * @param parameter configuration parameter
 * @param value parameter value
 */
@Entity
@Table(
        name = "configuracion_ia_simulacion",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {
                                "sesion_id",
                                "parametro"
                        }
                )
        }
)
@Data
public class SimulationConfiguration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "parametro", nullable = false)
    private String parameter;

    @Column(name = "valor", nullable = false)
    private String value;

    @ManyToOne
    @JoinColumn(name = "sesion_id", nullable = false)
    private SimulationSession session;
}