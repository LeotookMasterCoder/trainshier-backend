package com.trainshier.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Generic response message DTO.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponseDTO {

    /**
     * Response message.
     */
    private String message;
}