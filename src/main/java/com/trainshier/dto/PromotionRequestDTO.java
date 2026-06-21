package com.trainshier.dto;

import lombok.Data;

/**
 * @param name promotion name
 * @param type promotion type
 * @param discount discount percentage
 * @param minimumAmount minimum amount required
 */
@Data
public class PromotionRequestDTO {

    private String name;

    private String type;

    private Double discount;

    private Double minimumAmount;
}