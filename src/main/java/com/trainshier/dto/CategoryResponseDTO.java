package com.trainshier.dto;

import lombok.Data;

/**
 * @param id category identifier
 * @param name category name
 */
@Data
public class CategoryResponseDTO {

    private Long id;

    private String name;
}