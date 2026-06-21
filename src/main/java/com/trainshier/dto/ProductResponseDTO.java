package com.trainshier.dto;

import lombok.Data;

@Data
public class ProductResponseDTO {

    private Long id;

    private String name;

    private Double price;

    private Integer stock;

    private String barcode;

    private Boolean active;
}