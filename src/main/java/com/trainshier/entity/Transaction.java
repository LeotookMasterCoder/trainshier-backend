package com.trainshier.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;
import com.trainshier.enums.TransactionStatus;
import com.trainshier.converter.TransactionStatusConverter;


@Entity
@Table(name = "transacciones")
@Data
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "estado", nullable = false)
    @Convert(converter = TransactionStatusConverter.class)
    private TransactionStatus status;

    @Column(name = "total")
    private Double total;

    @Column(name = "errores")
    private Integer errors;

    @Column(name = "efectividad", nullable = false)
    private Double effectiveness;

    @Column(name = "fecha")
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "acceso_id", nullable = false)
    private SessionAccess access;
}