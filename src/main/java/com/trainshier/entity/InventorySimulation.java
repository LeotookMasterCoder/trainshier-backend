package com.trainshier.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "inventario_simulado")
@Data
public class InventorySimulation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_inv")
    private Long id;

    @Column(name = "stock_inicial", nullable = false)
    private Integer initialStock;

    @Column(name = "stock_actual", nullable = false)
    private Integer currentStock;

    @ManyToOne
    @JoinColumn(name = "acceso_id", nullable = false)
    private SessionAccess access;

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Product product;
}