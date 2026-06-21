package com.trainshier.dto;

import lombok.Data;

@Data
public class ChatRequestDTO {
    private String customerName;
    private String mood;
    private String difficulty;
    private String cartProducts;
    private int patience;
    private String message;
}
