package com.trainshier.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "invoices")
@Data
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "invoice_number", unique = true, nullable = false)
    private String invoiceNumber;

    @Column(name = "receipt_url")
    private String receiptUrl;

    @Column(name = "generated_at")
    private LocalDateTime generatedAt;

    @OneToOne
    @JoinColumn(name = "transaction_id", unique = true, nullable = false)
    private Transaction transaction;
}