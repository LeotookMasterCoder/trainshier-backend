package com.trainshier.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "facturas")
@Data
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "numero_factura", unique = true, nullable = false)
    private String invoiceNumber;

    @Column(name = "url_comprobante")
    private String receiptUrl;

    @Column(name = "generada_en")
    private LocalDateTime generatedAt;

    @OneToOne
    @JoinColumn(name = "transaccion_id", unique = true, nullable = false)
    private Transaction transaction;
}