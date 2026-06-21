package com.trainshier.dto;

import lombok.Data;

/**
 * DTO returned when refreshing a JWT token.
 *
 * @param message operation result message
 * @param token new jwt token
 */
@Data
public class RefreshTokenResponseDTO {

    private String message;

    private String token;
}