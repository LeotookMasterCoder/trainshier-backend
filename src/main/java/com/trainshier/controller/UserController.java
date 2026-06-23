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

    /**
     * Create instructor account.
     */
    @PostMapping("/instructors")
    public MessageResponseDTO createInstructor(
            @RequestBody UserRequestDTO request) {
        return userService.createInstructor(request);
    }

    /**
     * Update user details as Administrator (includes role and rfidUid).
     */
    @PutMapping("/{id}/admin")
    public UserResponseDTO updateUserAdmin(
            @PathVariable Long id,
            @RequestBody UpdateUserRequestDTO request) {
        return userService.updateUserAdmin(id, request);
    }

    /**
     * Delete user by id (cascading).
     */
    @DeleteMapping("/{id}")
    public MessageResponseDTO deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }

    /**
     * Truncate and reset training tables.
     */
    @PostMapping("/truncate")
    public MessageResponseDTO truncateTrainingData() {
        return userService.truncateTrainingData();
    }
}