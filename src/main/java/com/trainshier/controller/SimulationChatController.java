package com.trainshier.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trainshier.dto.ChatRequestDTO;
import com.trainshier.dto.ChatResponseDTO;
import com.trainshier.service.GeminiService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/simulation/chat")
@RequiredArgsConstructor
public class SimulationChatController {

    private final GeminiService geminiService;

    @PostMapping
    public ResponseEntity<ChatResponseDTO> talkToCustomer(@RequestBody ChatRequestDTO request) {
        String botReply = geminiService.generateCustomerResponse(
            request.getCustomerName(),
            request.getMood(),
            request.getDifficulty(),
            request.getCartProducts(),
            request.getPatience(),
            request.getMessage()
        );

        ChatResponseDTO response = new ChatResponseDTO();
        response.setResponse(botReply);

        return ResponseEntity.ok(response);
    }
}
