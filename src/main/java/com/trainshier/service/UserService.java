package com.trainshier.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.trainshier.dto.MessageResponseDTO;
import com.trainshier.dto.UserRequestDTO;
import com.trainshier.dto.UserResponseDTO;
import com.trainshier.entity.User;
import com.trainshier.repository.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * User service.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Create user.
     *
     * @param request user request
     * @return response message
     */
    public MessageResponseDTO createUser(
            UserRequestDTO request) {

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()));

        user.setRole(request.getRole());

        userRepository.save(user);

        return new MessageResponseDTO(
                "User created successfully");
    }

    /**
     * Get all users.
     *
     * @return user list
     */
    public List<UserResponseDTO> findAll() {

        return userRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Find user by id.
     *
     * @param id user identifier
     * @return user dto
     */
    public UserResponseDTO findById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"));

        return mapToDTO(user);
    }

    /**
     * Map entity to dto.
     *
     * @param user user entity
     * @return dto
     */
    private UserResponseDTO mapToDTO(
            User user) {

        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }
}