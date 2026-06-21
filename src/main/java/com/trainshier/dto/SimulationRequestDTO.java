package com.trainshier.dto;

import lombok.Data;

/**
 * @param sessionId session identifier
 * @param ApprenticeId Apprentice identifier
 * @param difficulty difficulty level
 */
@Data
public class SimulationRequestDTO {

    private Long sessionId;

    private Long ApprenticeId;

    private String difficulty;
}