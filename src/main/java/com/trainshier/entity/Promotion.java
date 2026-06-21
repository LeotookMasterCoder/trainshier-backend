package com.trainshier.entity;

import jakarta.persistence.*;
import lombok.Data;
import com.trainshier.enums.PromotionType;
import com.trainshier.converter.PromotionTypeConverter;

@Entity
@Table(name = "promociones")
@Data
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre", nullable = false)
    private String name;

    @Column(name = "tipo", nullable = false)
    @Convert(converter = PromotionTypeConverter.class)
    private PromotionType type;

    @Column(name = "descuento", nullable = false)
    private Double discount;

    @Column(name = "monto_minimo")
    private Double minimumAmount;

    @Column(name = "activa")
    private Boolean active;
}