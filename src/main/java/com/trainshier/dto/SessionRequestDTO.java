package com.trainshier.dto;

import lombok.Data;

/**
 * @param instructorId instructor identifier
 * @param practiceId practice identifier
 */
@Data
public class SessionRequestDTO {

    private Long instructorId;

    private Long practiceId;
}