package com.trainshier.dto;

import lombok.Data;

/**
 * @param productId product identifier
 * @param quantity quantity purchased
 * @param unitPrice unit price
 */
@Data
public class SaleDetailDTO {

    private Long productId;

    private Integer quantity;

    private Double unitPrice;
}