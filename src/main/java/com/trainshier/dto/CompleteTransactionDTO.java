package com.trainshier.dto;

import lombok.Data;

import java.util.List;

/**
 * @param userId user identifier
 * @param total total amount
 * @param paymentMethod payment method
 * @param duration simulation duration
 * @param score trainee score
 * @param effectiveness effectiveness percentage
 * @param details purchased products
 */
@Data
public class CompleteTransactionDTO {

    private Long userId;

    private Double total;

    private String paymentMethod;

    private Double duration;

    private Integer score;

    private Double effectiveness;

    private List<SaleDetailDTO> details;
}