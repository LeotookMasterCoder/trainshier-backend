package com.trainshier.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;

import com.trainshier.dto.MessageResponseDTO;
import com.trainshier.dto.UserRequestDTO;
import com.trainshier.dto.UserResponseDTO;
import com.trainshier.dto.UpdateUserRequestDTO;
import com.trainshier.entity.RfidRequest;
import com.trainshier.entity.User;
import com.trainshier.enums.UserRole;
import com.trainshier.repository.RfidRequestRepository;
import com.trainshier.repository.UserRepository;

import lombok.RequiredArgsConstructor;

/** User service — full admin control. */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RfidRequestRepository rfidRequestRepository;
    private final PasswordEncoder passwordEncoder;
    private final JdbcTemplate jdbcTemplate;

    // -----------------------------------------------------------------------
    // CRUD
    // -----------------------------------------------------------------------

    public MessageResponseDTO createUser(UserRequestDTO request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("El correo ya está registrado");
        }
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole() != null ? request.getRole() : UserRole.APPRENTICE);
        user.setRfidUid(request.getRfidUid());
        user.setActive(true);
        userRepository.save(user);
        return new MessageResponseDTO("Usuario creado con éxito");
    }

    public List<UserResponseDTO> findAll() {
        return userRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public UserResponseDTO findById(Long id) {
        return mapToDTO(userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado")));
    }

    /** Update own profile (name + email only). */
    public UserResponseDTO updateUser(Long id, UpdateUserRequestDTO request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Optional<User> emailCheck = userRepository.findByEmail(request.getEmail());
        if (emailCheck.isPresent() && !emailCheck.get().getId().equals(id)) {
            throw new RuntimeException("El correo ya está en uso por otro usuario");
        }

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        userRepository.save(user);
        return mapToDTO(user);
    }

    /** Admin: create instructor account. */
    public MessageResponseDTO createInstructor(UserRequestDTO request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("El correo ya está registrado");
        }
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(UserRole.INSTRUCTOR);
        user.setRfidUid(request.getRfidUid());
        user.setActive(true);
        userRepository.save(user);
        return new MessageResponseDTO("Instructor creado con éxito");
    }

    /** Admin: create any user with any role. */
    public MessageResponseDTO createAnyUser(UserRequestDTO request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("El correo ya está registrado");
        }
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole() != null ? request.getRole() : UserRole.INSTRUCTOR);
        user.setRfidUid(request.getRfidUid());
        user.setActive(true);
        userRepository.save(user);
        return new MessageResponseDTO("Usuario creado con éxito");
    }

    /** Admin: update user (role, rfid, active). */
    public UserResponseDTO updateUserAdmin(Long id, UpdateUserRequestDTO request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Optional<User> emailCheck = userRepository.findByEmail(request.getEmail());
        if (emailCheck.isPresent() && !emailCheck.get().getId().equals(id)) {
            throw new RuntimeException("El correo ya está en uso por otro usuario");
        }

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        if (request.getRole() != null) {
            user.setRole(request.getRole());
        }
        user.setRfidUid(request.getRfidUid());
        if (request.getActive() != null) {
            user.setActive(request.getActive());
        }
        userRepository.save(user);
        return mapToDTO(user);
    }

    /** Admin: toggle user active/inactive. */
    public UserResponseDTO toggleActive(Long id, boolean active) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        user.setActive(active);
        userRepository.save(user);
        return mapToDTO(user);
    }

    /** Admin: delete user (cascade handled by DB trigger). */
    public MessageResponseDTO deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        userRepository.delete(user);
        return new MessageResponseDTO("Usuario eliminado con éxito");
    }

    /** Admin: truncate all training/simulation tables. */
    public MessageResponseDTO truncateTrainingData() {
        try {
            jdbcTemplate.execute(
                "TRUNCATE TABLE transaction_payments, transaction_details, simulation_errors, " +
                "instructor_comments, invoices, transactions, inventory_simulation, " +
                "session_access, simulation_configuration, reports, rfid_requests, " +
                "simulation_sessions RESTART IDENTITY CASCADE;"
            );
            return new MessageResponseDTO("Tablas de entrenamiento reiniciadas con éxito");
        } catch (Exception e) {
            throw new RuntimeException("Error al reiniciar tablas: " + e.getMessage());
        }
    }

    // -----------------------------------------------------------------------
    // RFID REQUESTS
    // -----------------------------------------------------------------------

    public List<RfidRequest> getPendingRfidRequests() {
        return rfidRequestRepository.findByStatus("pending");
    }

    public MessageResponseDTO submitRfidRequest(Long userId, String rfidUid) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        RfidRequest req = new RfidRequest();
        req.setUser(user);
        req.setRfidUid(rfidUid);
        req.setStatus("pending");
        req.setRequestedAt(LocalDateTime.now());
        rfidRequestRepository.save(req);
        return new MessageResponseDTO("Solicitud de RFID enviada. El administrador la revisará.");
    }

    public MessageResponseDTO reviewRfidRequest(Long requestId, String status, Long adminId, String notes) {
        RfidRequest req = rfidRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Administrador no encontrado"));

        req.setStatus(status);
        req.setReviewedAt(LocalDateTime.now());
        req.setReviewedBy(admin);
        req.setNotes(notes);
        rfidRequestRepository.save(req);

        // If approved: assign rfid_uid to the user
        if ("approved".equals(status)) {
            User target = req.getUser();
            target.setRfidUid(req.getRfidUid());
            userRepository.save(target);
        }
        return new MessageResponseDTO("Solicitud " + status + " correctamente");
    }

    // -----------------------------------------------------------------------
    // MAPPING
    // -----------------------------------------------------------------------

    private UserResponseDTO mapToDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getRfidUid(),
                user.getActive() != null ? user.getActive() : true
        );
    }
}