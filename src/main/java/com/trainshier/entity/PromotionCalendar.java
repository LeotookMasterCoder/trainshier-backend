package com.trainshier.entity;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.Data;

/**
 * @param id calendar identifier
 * @param promotion associated promotion
 * @param product associated product
 * @param startDate promotion start date
 * @param endDate promotion end date
 * @param weekDays active week days
 */
@Entity
@Table(name = "calendario_promociones")
@Data
public class PromotionCalendar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate startDate;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDate endDate;

    @Column(name = "dias_semana")
    private String weekDays;

    @ManyToOne
    @JoinColumn(name = "promocion_id", nullable = false)
    private Promotion promotion;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Product product;
}