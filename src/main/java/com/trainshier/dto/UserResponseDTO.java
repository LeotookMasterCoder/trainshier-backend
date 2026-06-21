package com.trainshier.dto;

import com.trainshier.enums.UserRole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User response DTO.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    /**
     * User identifier.
     */
    private Long id;

    /**
     * User full name.
     */
    private String name;

    /**
     * User email.
     */
    private String email;

    /**
     * User role.
     */
    private UserRole role;
}