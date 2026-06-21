package com.trainshier.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO used for user authentication.
 *
 * @param email user email
 * @param password user password
 */
@Data
public class LoginRequestDTO {

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;
}