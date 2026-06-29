package com.trainshier.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.trainshier.dto.LoginRequestDTO;
import com.trainshier.dto.LoginResponseDTO;
import com.trainshier.dto.MessageResponseDTO;
import com.trainshier.dto.RefreshTokenResponseDTO;
import com.trainshier.dto.RegisterRequestDTO;
import com.trainshier.dto.RecoverPasswordRequestDTO;
import com.trainshier.dto.RfidLoginRequestDTO;
import com.trainshier.dto.UserResponseDTO;
import com.trainshier.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import java.util.List;


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
     * Recovers password for a user.
     *
     * @param request recover password request DTO
     * @return response message
     */
    @PostMapping("/recover-password")
    public ResponseEntity<MessageResponseDTO> recoverPassword(
            @RequestBody RecoverPasswordRequestDTO request) {

        MessageResponseDTO response =
                authService.recoverPassword(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/recover-password/request")
    public ResponseEntity<MessageResponseDTO> requestRecoveryCode(@RequestParam String email) {
        MessageResponseDTO response = authService.requestRecoveryCode(email);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/recover-password/verify")
    public ResponseEntity<MessageResponseDTO> verifyRecoveryCode(
            @RequestParam String email,
            @RequestParam String code) {
        MessageResponseDTO response = authService.verifyRecoveryCode(email, code);
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

    /**
     * Returns the list of instructors for the public registration flow.
     * No authentication required - used by the apprentice registration form.
     *
     * @return list of instructor users
     */
    @GetMapping("/instructors")
    public ResponseEntity<List<UserResponseDTO>> getPublicInstructors() {
        return ResponseEntity.ok(authService.getPublicInstructors());
    }
}