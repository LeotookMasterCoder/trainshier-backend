package com.trainshier.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "transactions")
@Data
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "total")
    private Double total;

    @Column(name = "errors")
    private Integer errors;

    @Column(name = "effectiveness", nullable = false)
    private Double effectiveness;

    @Column(name = "date")
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "access_id")
    private SessionAccess access;
}