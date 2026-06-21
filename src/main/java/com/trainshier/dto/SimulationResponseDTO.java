package com.trainshier.dto;

import lombok.Data;

/**
 * @param score obtained score
 * @param effectiveness effectiveness percentage
 * @param customerSatisfaction satisfaction percentage
 * @param feedback generated feedback
 */
@Data
public class SimulationResponseDTO {

    private Integer score;

    private Double effectiveness;

    private Integer customerSatisfaction;

    private String feedback;
}