package com.trainshier.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.trainshier.dto.LoginRequestDTO;
import com.trainshier.dto.LoginResponseDTO;
import com.trainshier.dto.MessageResponseDTO;
import com.trainshier.dto.RefreshTokenResponseDTO;
import com.trainshier.dto.RegisterRequestDTO;
import com.trainshier.dto.RecoverPasswordRequestDTO;
import com.trainshier.dto.RfidLoginRequestDTO;
import com.trainshier.dto.UserResponseDTO;
import com.trainshier.entity.User;
import com.trainshier.enums.UserRole;
import com.trainshier.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Authentication service.
 *
 * @param passwordEncoder password encoder
 * @param userRepository user repository
 * @param jwtService jwt service
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final EmailService emailService;
    private final RateLimiterService rateLimiterService;
    private final jakarta.servlet.http.HttpServletRequest httpServletRequest;
    private final Map<String, String> recoveryCodes = new ConcurrentHashMap<>();

    private String getClientIp() {
        if (httpServletRequest == null) {
            return "unknown-ip";
        }
        String ipAddress = httpServletRequest.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = httpServletRequest.getRemoteAddr();
        } else {
            ipAddress = ipAddress.split(",")[0].trim();
        }
        return ipAddress;
    }

    /**
     * Register a new user.
     *
     * @param request registration data
     * @return response message
     */
    public MessageResponseDTO register(RegisterRequestDTO request) {
        String ipKey = getClientIp() + ":register";
        rateLimiterService.checkBlocked(ipKey);

        try {
            if (userRepository.findByEmail(request.getEmail()).isPresent()) {
                throw new RuntimeException("El correo electrónico ya está registrado.");
            }

            if (request.getUsername() != null && request.getUsername().contains("#")) {
                String suffix = request.getUsername().substring(request.getUsername().indexOf("#"));
                java.util.List<User> list = userRepository.findAll();
                for (User u : list) {
                    if (u.getUsername() != null && u.getUsername().endsWith(suffix)) {
                        throw new RuntimeException("El numeral con los números ya está en uso.");
                    }
                }
            }

            User user = new User();
            user.setName(request.getName());
            user.setEmail(request.getEmail());
            user.setUsername(request.getUsername());
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
            response.setMessage("Usuario registrado con éxito");

            rateLimiterService.resetAttempts(ipKey);
            return response;
        } catch (RuntimeException e) {
            rateLimiterService.recordFailedAttempt(ipKey);
            throw e;
        }
    }

    /**
     * Login user.
     *
     * @param request login request
     * @return login response
     */
    public LoginResponseDTO login(LoginRequestDTO request) {
        String ipKey = getClientIp() + ":login";
        rateLimiterService.checkBlocked(ipKey);

        try {
            Optional<User> optionalUser =
                    userRepository.findByEmail(request.getEmail());

            if (optionalUser.isEmpty()) {
                throw new RuntimeException("Usuario no encontrado.");
            }

            User user = optionalUser.get();

            if (!passwordEncoder.matches(
                    request.getPassword(),
                    user.getPassword())) {

                throw new RuntimeException("Contraseña incorrecta.");
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
            response.setEmail(user.getEmail());
            response.setUsername(user.getUsername());
            response.setRole(user.getRole().name());

            rateLimiterService.resetAttempts(ipKey);
            return response;
        } catch (RuntimeException e) {
            rateLimiterService.recordFailedAttempt(ipKey);
            throw e;
        }
    }

    /**
     * Login user using RFID card.
     *
     * @param request RFID login request
     * @return login response
     */
    public LoginResponseDTO rfidLogin(RfidLoginRequestDTO request) {
        String ipKey = getClientIp() + ":login";
        rateLimiterService.checkBlocked(ipKey);

        try {
            Optional<User> optionalUser =
                    userRepository.findByRfidUid(request.getRfidUid());

            if (optionalUser.isEmpty()) {
                throw new RuntimeException("Tarjeta RFID no asociada a ningún usuario");
            }

            User user = optionalUser.get();

            String token = jwtService.generateToken(
                    user.getId(),
                    user.getName(),
                    user.getRole().name());

            LoginResponseDTO response = new LoginResponseDTO();
            response.setMessage("Login successful via RFID");
            response.setToken(token);
            response.setUserId(user.getId());
            response.setName(user.getName());
            response.setEmail(user.getEmail());
            response.setUsername(user.getUsername());
            response.setRole(user.getRole().name());

            rateLimiterService.resetAttempts(ipKey);
            return response;
        } catch (RuntimeException e) {
            rateLimiterService.recordFailedAttempt(ipKey);
            throw e;
        }
    }

    /**
     * Recovers/resets user password.
     *
     * @param request recover password request DTO
     * @return response message
     */
    public MessageResponseDTO recoverPassword(RecoverPasswordRequestDTO request) {
        String ipKey = getClientIp() + ":recover";
        rateLimiterService.checkBlocked(ipKey);

        try {
            Optional<User> optionalUser =
                    userRepository.findByEmail(request.getEmail());

            if (optionalUser.isEmpty()) {
                throw new RuntimeException("Usuario no encontrado");
            }

            User user = optionalUser.get();
            // Encrypt the new password securely
            String encryptedPassword = passwordEncoder.encode(request.getNewPassword());
            user.setPassword(encryptedPassword);
            userRepository.save(user);

            // Send functional password recovery email
            emailService.sendPasswordRecoveryEmail(user.getEmail(), user.getName(), request.getNewPassword());

            MessageResponseDTO response = new MessageResponseDTO();
            response.setMessage("Contraseña restablecida y guardada correctamente. Se envió confirmación al correo.");
            
            rateLimiterService.resetAttempts(ipKey);
            return response;
        } catch (RuntimeException e) {
            rateLimiterService.recordFailedAttempt(ipKey);
            throw e;
        }
    }

    public MessageResponseDTO requestRecoveryCode(String email) {
        String ipKey = getClientIp() + ":recover";
        rateLimiterService.checkBlocked(ipKey);

        try {
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if (optionalUser.isEmpty()) {
                throw new RuntimeException("Usuario no registrado con ese correo.");
            }
            User user = optionalUser.get();

            // Generate 6-digit code
            String code = String.format("%06d", new Random().nextInt(1000000));
            recoveryCodes.put(email, code);

            // Send recovery code email
            boolean sent = emailService.sendRecoveryCodeEmail(user.getEmail(), user.getName(), code);

            MessageResponseDTO response = new MessageResponseDTO();
            if (sent) {
                response.setMessage("Código de verificación enviado al correo.");
            } else {
                log.info("CÓDIGO DE VERIFICACIÓN GENERADO (Mailgun no configurado): {} para {}", code, email);
                response.setMessage("Código de verificación enviado (simulado en consola del servidor).");
            }
            return response;
        } catch (RuntimeException e) {
            rateLimiterService.recordFailedAttempt(ipKey);
            throw e;
        }
    }

    public MessageResponseDTO verifyRecoveryCode(String email, String code) {
        String ipKey = getClientIp() + ":recover";
        rateLimiterService.checkBlocked(ipKey);

        try {
            String savedCode = recoveryCodes.get(email);
            if (savedCode == null || !savedCode.equals(code)) {
                throw new RuntimeException("Código de verificación inválido");
            }

            MessageResponseDTO response = new MessageResponseDTO();
            response.setMessage("Código verificado correctamente.");
            return response;
        } catch (RuntimeException e) {
            rateLimiterService.recordFailedAttempt(ipKey);
            throw e;
        }
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

    /**
     * Returns the list of instructors available for the public registration form.
     * This method is intentionally public (no auth required).
     *
     * @return list of instructor user DTOs
     */
    public List<UserResponseDTO> getPublicInstructors() {
        return userRepository.findAll().stream()
                .filter(u -> u.getRole() == UserRole.INSTRUCTOR)
                .map(u -> new UserResponseDTO(u.getId(), u.getName(), u.getEmail(), u.getUsername(), u.getRole(), u.getRfidUid(), u.getActive() != null ? u.getActive() : true))
                .collect(Collectors.toList());
    }
}
