package com.trainshier.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "transaction_payments")
@Data
public class TransactionPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "payment_type", nullable = false)
    private String paymentType;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "delivered_change")
    private Double deliveredChange;

    @ManyToOne
    @JoinColumn(name = "transaction_id", nullable = false)
    private Transaction transaction;
}