package com.trainshier.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.*;

import com.trainshier.dto.MessageResponseDTO;
import com.trainshier.dto.UserRequestDTO;
import com.trainshier.dto.UserResponseDTO;
import com.trainshier.dto.UpdateUserRequestDTO;
import com.trainshier.entity.RfidRequest;
import com.trainshier.service.UserService;

import lombok.RequiredArgsConstructor;

/** User management controller. */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public MessageResponseDTO createUser(@RequestBody UserRequestDTO request) {
        return userService.createUser(request);
    }

    @GetMapping
    public List<UserResponseDTO> getUsers() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public UserResponseDTO getUser(@PathVariable Long id) {
        return userService.findById(id);
    }

    @PutMapping("/{id}")
    public UserResponseDTO updateUser(@PathVariable Long id,
                                      @RequestBody UpdateUserRequestDTO request) {
        return userService.updateUser(id, request);
    }

    /** Admin: create instructor. */
    @PostMapping("/instructors")
    public MessageResponseDTO createInstructor(@RequestBody UserRequestDTO request) {
        return userService.createInstructor(request);
    }

    /** Admin: create any user (instructor or administrator). */
    @PostMapping("/create-any")
    public MessageResponseDTO createAnyUser(@RequestBody UserRequestDTO request) {
        return userService.createAnyUser(request);
    }

    /** Admin: edit user data including role, rfid and active status. */
    @PutMapping("/{id}/admin")
    public UserResponseDTO updateUserAdmin(@PathVariable Long id,
                                           @RequestBody UpdateUserRequestDTO request) {
        return userService.updateUserAdmin(id, request);
    }

    /** Admin: toggle user active/inactive. */
    @PatchMapping("/{id}/toggle-active")
    public UserResponseDTO toggleActive(@PathVariable Long id,
                                         @RequestBody Map<String, Boolean> body) {
        boolean active = body.getOrDefault("active", true);
        return userService.toggleActive(id, active);
    }

    /** Admin: delete user (cascade in DB). */
    @DeleteMapping("/{id}")
    public MessageResponseDTO deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }

    /** Admin: truncate all training data. */
    @PostMapping("/truncate")
    public MessageResponseDTO truncateTrainingData() {
        return userService.truncateTrainingData();
    }

    // -----------------------------------------------------------------------
    // RFID REQUESTS
    // -----------------------------------------------------------------------

    /** Get all pending RFID requests (admin). */
    @GetMapping("/rfid-requests")
    public List<RfidRequest> getPendingRfidRequests() {
        return userService.getPendingRfidRequests();
    }

    /** Submit RFID request (apprentice). */
    @PostMapping("/rfid-requests")
    public MessageResponseDTO submitRfidRequest(@RequestBody Map<String, String> body) {
        Long userId = Long.parseLong(body.get("userId"));
        String rfidUid = body.get("rfidUid");
        return userService.submitRfidRequest(userId, rfidUid);
    }

    /** Review RFID request (admin: approve or reject). */
    @PatchMapping("/rfid-requests/{id}/review")
    public MessageResponseDTO reviewRfidRequest(@PathVariable Long id,
                                                 @RequestBody Map<String, String> body) {
        String status = body.get("status");
        Long adminId = Long.parseLong(body.get("adminId"));
        String notes = body.getOrDefault("notes", "");
        return userService.reviewRfidRequest(id, status, adminId, notes);
    }
}