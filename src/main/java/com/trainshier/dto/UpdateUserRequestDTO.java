package com.trainshier.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO for updating user profile info (name and email).
 */
@Data
public class UpdateUserRequestDTO {

    /**
     * User full name.
     */
    @NotBlank(message = "Name is required")
    private String name;

    /**
     * User email.
     */
    @Email(message = "Invalid email")
    @NotBlank(message = "Email is required")
    private String email;

    /**
     * User username.
     */
    private String username;

    /**
     * User role.
     */
    private com.trainshier.enums.UserRole role;

    /**
     * User RFID UID.
     */
    private String rfidUid;

    /**
     * Whether the user account is active.
     */
    private Boolean active;
}
