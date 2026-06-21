package com.trainshier.dto;

import lombok.Data;

/**
 * @param name customer name
 * @param mood customer mood
 * @param patience patience level
 * @param request customer request
 * @param message generated message
 */
@Data
public class CustomerResponseDTO {

    private String name;

    private String mood;

    private Integer patience;

    private String request;

    private String message;
}