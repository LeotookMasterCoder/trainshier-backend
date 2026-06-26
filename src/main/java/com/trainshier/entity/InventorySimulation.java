package com.trainshier.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "inventory_simulation")
@Data
public class InventorySimulation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "initial_stock", nullable = false)
    private Integer initialStock;

    @Column(name = "current_stock", nullable = false)
    private Integer currentStock;

    @ManyToOne
    @JoinColumn(name = "access_id", nullable = false)
    private SessionAccess access;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}