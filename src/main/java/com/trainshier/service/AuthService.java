package com.trainshier.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.trainshier.dto.LoginRequestDTO;
import com.trainshier.dto.LoginResponseDTO;
import com.trainshier.dto.MessageResponseDTO;
import com.trainshier.dto.RefreshTokenResponseDTO;
import com.trainshier.dto.RegisterRequestDTO;
import com.trainshier.entity.User;
import com.trainshier.enums.UserRole;
import com.trainshier.repository.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * Authentication service.
 *
 * @param passwordEncoder password encoder
 * @param userRepository user repository
 * @param jwtService jwt service
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    /**
     * Register a new user.
     *
     * @param request registration data
     * @return response message
     */
    public MessageResponseDTO register(RegisterRequestDTO request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        
        UserRole userRole = UserRole.APPRENTICE;
        if (request.getRole() != null) {
            String roleStr = request.getRole().toUpperCase();
            if (roleStr.contains("APRENDIZ") || roleStr.contains("APPRENTICE")) {
                userRole = UserRole.APPRENTICE;
            } else if (roleStr.contains("INSTRUCTOR")) {
                userRole = UserRole.INSTRUCTOR;
            } else if (roleStr.contains("ADMINISTRADOR") || roleStr.contains("ADMINISTRATOR") || roleStr.contains("ADMIN")) {
                userRole = UserRole.ADMINISTRATOR;
            }
        }
        user.setRole(userRole);

        userRepository.save(user);

        MessageResponseDTO response = new MessageResponseDTO();
        response.setMessage("User registered successfully");

        return response;
    }

    /**
     * Login user.
     *
     * @param request login request
     * @return login response
     */
    public LoginResponseDTO login(LoginRequestDTO request) {

        Optional<User> optionalUser =
                userRepository.findByEmail(request.getEmail());

        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = optionalUser.get();

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {

            throw new RuntimeException("Invalid password");
        }

        String token = jwtService.generateToken(
                user.getId(),
                user.getName(),
                user.getRole().name());

        LoginResponseDTO response = new LoginResponseDTO();
        response.setMessage("Login successful");
        response.setToken(token);
        response.setUserId(user.getId());
        response.setName(user.getName());
        response.setRole(user.getRole().name());

        return response;
    }

    /**
     * Refresh JWT token.
     *
     * @param token old token
     * @return refreshed token
     */
    public RefreshTokenResponseDTO refreshToken(String token) {

        String newToken = jwtService.refreshToken(token);

        RefreshTokenResponseDTO response =
                new RefreshTokenResponseDTO();

        response.setMessage("Token refreshed");
        response.setToken(newToken);

        return response;
    }
}
