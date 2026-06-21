package com.trainshier.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @param id session identifier
 * @param accessCode generated code
 * @param active session status
 * @param expiresAt expiration date
 */
@Data
public class SessionResponseDTO {

    private Long id;

    private String accessCode;

    private Boolean active;

    private LocalDateTime expiresAt;
}