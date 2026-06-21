package com.trainshier.dto;

import lombok.Data;

/**
 * @param productId product identifier
 * @param productName product name
 * @param initialStock initial stock
 * @param currentStock current stock
 */
@Data
public class InventoryResponseDTO {

    private Long productId;

    private String productName;

    private Integer initialStock;

    private Integer currentStock;
}