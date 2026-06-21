package com.trainshier.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.trainshier.dto.LoginRequestDTO;
import com.trainshier.dto.LoginResponseDTO;
import com.trainshier.dto.MessageResponseDTO;
import com.trainshier.dto.RefreshTokenResponseDTO;
import com.trainshier.dto.RegisterRequestDTO;
import com.trainshier.dto.RfidLoginRequestDTO;
import com.trainshier.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

/**
 * Authentication controller.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Registers a new user.
     *
     * @param request registration data
     * @return registration response
     */
    @PostMapping("/register")
    public ResponseEntity<MessageResponseDTO> register(
            @RequestBody RegisterRequestDTO request) {

        MessageResponseDTO response =
                authService.register(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    /**
     * Authenticates a user.
     *
     * @param request login request
     * @return login response
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @RequestBody LoginRequestDTO request) {

        LoginResponseDTO response =
                authService.login(request);

        return ResponseEntity.ok(response);
    }

    /**
     * Authenticates a user using RFID.
     *
     * @param request RFID login request
     * @return login response
     */
    @PostMapping("/rfid-login")
    public ResponseEntity<LoginResponseDTO> rfidLogin(
            @RequestBody RfidLoginRequestDTO request) {

        LoginResponseDTO response =
                authService.rfidLogin(request);

        return ResponseEntity.ok(response);
    }

    /**
     * Refreshes JWT token.
     *
     * @param request HTTP request
     * @return refreshed token
     */
    @GetMapping("/refreshToken")
    public ResponseEntity<RefreshTokenResponseDTO> refreshToken(
            HttpServletRequest request) {

        String authHeader =
                request.getHeader("Authorization");

        if (authHeader == null ||
                !authHeader.startsWith("Bearer ")) {

            return ResponseEntity.badRequest().build();
        }

        String token =
                authHeader.substring(7);

        RefreshTokenResponseDTO response =
                authService.refreshToken(token);

        return ResponseEntity.ok(response);
    }
}