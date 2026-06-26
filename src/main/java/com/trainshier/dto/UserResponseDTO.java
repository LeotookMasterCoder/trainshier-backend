package com.trainshier.dto;

import com.trainshier.enums.UserRole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** User response DTO. */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    private Long id;
    private String name;
    private String email;
    private UserRole role;
    private String rfidUid;
    private Boolean active;
}