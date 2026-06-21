package com.trainshier.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;
import com.trainshier.converter.InventoryMovementTypeConverter;
import com.trainshier.enums.InventoryMovementType;

/**
 * @param id movement identifier
 * @param product affected product
 * @param movementType movement type
 * @param quantity movement quantity
 * @param date movement date
 * @param reason movement reason
 */
@Entity
@Table(name = "movimientos_inventario")
@Data
public class InventoryMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "tipo", nullable = false)
    @Convert(converter = InventoryMovementTypeConverter.class)
    private InventoryMovementType movementType;

    @Column(name = "cantidad", nullable = false)
    private Integer quantity;

    @Column(name = "fecha")
    private LocalDateTime date;

    @Column(name = "motivo")
    private String reason;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Product product;
}