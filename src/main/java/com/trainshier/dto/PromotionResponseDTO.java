package com.trainshier.dto;

import lombok.Data;

/**
 * @param id promotion identifier
 * @param name promotion name
 * @param type promotion type
 * @param discount discount percentage
 * @param active promotion status
 */
@Data
public class PromotionResponseDTO {

    private Long id;

    private String name;

    private String type;

    private Double discount;

    private Boolean active;
}