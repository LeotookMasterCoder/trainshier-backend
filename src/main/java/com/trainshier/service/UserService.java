package com.trainshier.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;
import com.trainshier.enums.UserRole;

import com.trainshier.dto.MessageResponseDTO;
import com.trainshier.dto.UserRequestDTO;
import com.trainshier.dto.UserResponseDTO;
import com.trainshier.dto.UpdateUserRequestDTO;
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
    private final JdbcTemplate jdbcTemplate;

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
        user.setRfidUid(request.getRfidUid());

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
     * Update user details (profile).
     *
     * @param id user identifier
     * @param request update request
     * @return updated user dto
     */
    public UserResponseDTO updateUser(Long id, UpdateUserRequestDTO request) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"));

        Optional<User> existingEmailUser = userRepository.findByEmail(request.getEmail());
        if (existingEmailUser.isPresent() && !existingEmailUser.get().getId().equals(id)) {
            throw new RuntimeException("Email already taken by another user");
        }

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        userRepository.save(user);

        return mapToDTO(user);
    }

    /**
     * Create instructor by administrator.
     */
    public MessageResponseDTO createInstructor(UserRequestDTO request) {
        Optional<User> existing = userRepository.findByEmail(request.getEmail());
        if (existing.isPresent()) {
            throw new RuntimeException("El correo ya está registrado");
        }
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(UserRole.INSTRUCTOR);
        user.setRfidUid(request.getRfidUid());
        userRepository.save(user);
        return new MessageResponseDTO("Instructor creado con éxito");
    }

    /**
     * Delete user by id (cascades via DB trigger).
     */
    public MessageResponseDTO deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        userRepository.delete(user);
        return new MessageResponseDTO("Usuario eliminado con éxito");
    }

    /**
     * Update user details as Administrator.
     */
    public UserResponseDTO updateUserAdmin(Long id, UpdateUserRequestDTO request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Optional<User> existingEmailUser = userRepository.findByEmail(request.getEmail());
        if (existingEmailUser.isPresent() && !existingEmailUser.get().getId().equals(id)) {
            throw new RuntimeException("El correo ya está registrado por otro usuario");
        }

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        if (request.getRole() != null) {
            user.setRole(request.getRole());
        }
        user.setRfidUid(request.getRfidUid());
        userRepository.save(user);

        return mapToDTO(user);
    }

    /**
     * Truncate and reset all training statistics.
     */
    public MessageResponseDTO truncateTrainingData() {
        try {
            jdbcTemplate.execute("TRUNCATE TABLE pagos_transaccion, detalle_transaccion, errores_simulacion, comentarios_instructor, transacciones, accesos_sesion, reports, sesiones_simulacion RESTART IDENTITY CASCADE;");
            return new MessageResponseDTO("Tablas de entrenamiento reiniciadas con éxito");
        } catch (Exception e) {
            throw new RuntimeException("Error al reiniciar las tablas de entrenamiento: " + e.getMessage());
        }
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
                user.getRole(),
                user.getRfidUid()
        );
    }
}