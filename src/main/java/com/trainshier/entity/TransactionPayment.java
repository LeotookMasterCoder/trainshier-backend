package com.trainshier.entity;

import jakarta.persistence.*;
import lombok.Data;
import com.trainshier.converter.PaymentTypeConverter;
import com.trainshier.enums.PaymentType;

@Entity
@Table(name = "pagos_transaccion")
@Data
public class TransactionPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private Long id;

    @Column(name = "tipo_pago", nullable = false)
    @Convert(converter = PaymentTypeConverter.class)
    private PaymentType paymentType;

    @Column(name = "monto", nullable = false)
    private Double amount;

    @Column(name = "cambio_entregado")
    private Double deliveredChange;

    @ManyToOne
    @JoinColumn(name = "transaccion_id", nullable = false)
    private Transaction transaction;
}