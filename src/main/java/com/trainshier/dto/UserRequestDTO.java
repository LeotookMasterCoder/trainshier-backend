package com.trainshier.dto;

import com.trainshier.enums.UserRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * User request DTO.
 */
@Data
public class UserRequestDTO {

    /**
     * User full name.
     */
    @NotBlank
    private String name;

    /**
     * User email.
     */
    @Email
    @NotBlank
    private String email;

    /**
     * User password.
     */
    @NotBlank
    private String password;

    /**
     * User role.
     */
    private UserRole role;
}