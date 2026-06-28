package com.trainshier.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trainshier.service.GeminiService;

import lombok.RequiredArgsConstructor;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/assistant")
@RequiredArgsConstructor
public class AssistantController {

    private final GeminiService geminiService;

    @PostMapping("/chat")
    public ResponseEntity<Map<String, String>> talkToAssistant(@RequestBody Map<String, String> request) {
        String userMessage = request.getOrDefault("message", "");
        String reply = geminiService.generateAssistantResponse(userMessage);

        Map<String, String> response = new HashMap<>();
        response.put("response", reply);

        return ResponseEntity.ok(response);
    }
}
