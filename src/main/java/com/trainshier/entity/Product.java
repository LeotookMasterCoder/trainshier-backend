package com.trainshier.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "products")
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "stock", nullable = false)
    private Integer stock;

    @Column(name = "barcode", unique = true)
    private String barcode;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "iva")
    private Integer iva = 19;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}