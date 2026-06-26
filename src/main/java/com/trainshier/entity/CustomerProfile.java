package com.trainshier.entity;

import jakarta.persistence.*;
import lombok.Data;

/** AI customer profile used in POS simulation. */
@Entity
@Table(name = "customer_profiles")
@Data
public class CustomerProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "mood")
    private String mood;

    @Column(name = "patience")
    private Integer patience;

    @Column(name = "request_text")
    private String requestText;

    @Column(name = "message")
    private String message;
}