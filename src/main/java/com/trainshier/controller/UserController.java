package com.trainshier.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.trainshier.dto.MessageResponseDTO;
import com.trainshier.dto.UserRequestDTO;
import com.trainshier.dto.UserResponseDTO;
import com.trainshier.dto.UpdateUserRequestDTO;
import com.trainshier.service.UserService;

import lombok.RequiredArgsConstructor;

/**
 * User controller.
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Create user.
     *
     * @param request user request
     * @return message
     */
    @PostMapping
    public MessageResponseDTO createUser(
            @RequestBody UserRequestDTO request) {

        return userService.createUser(request);
    }

    /**
     * Get all users.
     *
     * @return users
     */
    @GetMapping
    public List<UserResponseDTO> getUsers() {

        return userService.findAll();
    }

    /**
     * Find user by id.
     *
     * @param id user identifier
     * @return user
     */
    @GetMapping("/{id}")
    public UserResponseDTO getUser(
            @PathVariable Long id) {

        return userService.findById(id);
    }

    /**
     * Update user by id.
     *
     * @param id user identifier
     * @param request update request DTO
     * @return updated user details DTO
     */
    @PutMapping("/{id}")
    public UserResponseDTO updateUser(
            @PathVariable Long id,
            @RequestBody UpdateUserRequestDTO request) {

        return userService.updateUser(id, request);
    }
}