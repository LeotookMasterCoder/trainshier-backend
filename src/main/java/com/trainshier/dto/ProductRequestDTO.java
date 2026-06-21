package com.trainshier.dto;

import lombok.Data;

/**
 * @param name product name
 * @param price product price
 * @param stock available stock
 * @param barcode barcode identifier
 * @param categoryId category identifier
 */
@Data
public class ProductRequestDTO {

    private String name;

    private Double price;

    private Integer stock;

    private String barcode;

    private Long categoryId;
}