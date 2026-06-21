package com.trainshier.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @param id report identifier
 * @param ApprenticeName Apprentice name
 * @param score score obtained
 * @param effectiveness effectiveness percentage
 * @param generatedAt generation date
 */
@Data
public class ReportResponseDTO {

    private Long id;

    private String ApprenticeName;

    private Double score;

    private Double effectiveness;

    private LocalDateTime generatedAt;
}