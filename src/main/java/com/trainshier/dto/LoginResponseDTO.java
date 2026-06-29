package com.trainshier.dto;

import lombok.Data;

/**
 * DTO returned after successful authentication.
 *
 * @param message operation result message
 * @param token jwt token
 * @param userId authenticated user id
 * @param name authenticated user name
 * @param role authenticated user role
 */
@Data
public class LoginResponseDTO {

    private String message;

    private String token;

    private Long userId;

    private String name;

    private String email;

    private String username;

    private String role;
}