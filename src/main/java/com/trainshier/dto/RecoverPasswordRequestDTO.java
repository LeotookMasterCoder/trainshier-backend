package com.trainshier.dto;

import lombok.Data;

@Data
public class RecoverPasswordRequestDTO {
    private String email;
    private String newPassword;
}
