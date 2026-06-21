package com.trainshier.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "detalle_transaccion")
@Data
public class TransactionDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle")
    private Long id;

    @Column(name = "cantidad", nullable = false)
    private Integer quantity;

    @Column(name = "precio_unitario", nullable = false)
    private Double unitPrice;

    @Column(name = "descuento_aplicado")
    private Double discountApplied;

    @ManyToOne
    @JoinColumn(name = "transaccion_id", nullable = false)
    private Transaction transaction;

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "promocion_id")
    private Promotion promotion;
}