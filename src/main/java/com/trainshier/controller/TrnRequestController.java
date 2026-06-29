package com.trainshier.controller;

import com.trainshier.entity.TrnRequest;
import com.trainshier.entity.User;
import com.trainshier.repository.TrnRequestRepository;
import com.trainshier.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TrnRequestController {

    private final TrnRequestRepository trnRequestRepository;
    private final UserRepository userRepository;

    // --- PUBLIC ENDPOINTS (under /auth/trn-requests, bypasses JWT filter) ---

    @PostMapping("/auth/trn-requests")
    public ResponseEntity<?> requestTrnCode(@RequestBody Map<String, Object> body) {
        try {
            Long instructorId = Long.parseLong(body.get("instructorId").toString());
            String studentName = body.get("studentName").toString();
            String studentEmail = body.get("studentEmail").toString();

            Optional<User> instructorOpt = userRepository.findById(instructorId);
            if (instructorOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "El instructor seleccionado no existe."));
            }

            // Check if user already has an account
            if (userRepository.findByEmail(studentEmail).isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "El correo electrónico ya está registrado en el sistema. Inicia sesión directamente."));
            }

            // Check if there is already a request for this email
            Optional<TrnRequest> existingOpt = trnRequestRepository.findByStudentEmail(studentEmail);
            TrnRequest trnRequest;
            if (existingOpt.isPresent()) {
                trnRequest = existingOpt.get();
                trnRequest.setInstructor(instructorOpt.get());
                trnRequest.setStudentName(studentName);
                trnRequest.setStatus("PENDING");
                trnRequest.setTrnCode(null);
                trnRequest.setRequestedAt(LocalDateTime.now());
                trnRequest.setReviewedAt(null);
            } else {
                trnRequest = new TrnRequest();
                trnRequest.setInstructor(instructorOpt.get());
                trnRequest.setStudentName(studentName);
                trnRequest.setStudentEmail(studentEmail);
                trnRequest.setStatus("PENDING");
                trnRequest.setRequestedAt(LocalDateTime.now());
            }

            trnRequestRepository.save(trnRequest);
            return ResponseEntity.ok(Map.of("message", "Solicitud de autorización TRN enviada correctamente."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error al procesar la solicitud: " + e.getMessage()));
        }
    }

    @GetMapping("/auth/trn-requests/check")
    public ResponseEntity<?> checkTrnStatus(@RequestParam String email) {
        Optional<TrnRequest> requestOpt = trnRequestRepository.findByStudentEmail(email);
        if (requestOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "No se encontró ninguna solicitud para este correo."));
        }
        return ResponseEntity.ok(requestOpt.get());
    }

    // --- PRIVATE ENDPOINTS (under /trn-requests, filtered by JWT filter) ---

    @GetMapping("/trn-requests/instructor/{instructorId}")
    public List<TrnRequest> getPendingRequests(@PathVariable Long instructorId) {
        return trnRequestRepository.findByInstructorIdAndStatus(instructorId, "PENDING");
    }

    @PutMapping("/trn-requests/{id}/approve")
    public ResponseEntity<?> approveRequest(@PathVariable Long id) {
        Optional<TrnRequest> requestOpt = trnRequestRepository.findById(id);
        if (requestOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Solicitud no encontrada."));
        }
        TrnRequest trnRequest = requestOpt.get();
        
        // Generate random code TRN-XXXX
        int random = 1000 + new Random().nextInt(9000);
        String code = "TRN-" + random;

        trnRequest.setTrnCode(code);
        trnRequest.setStatus("APPROVED");
        trnRequest.setReviewedAt(LocalDateTime.now());
        trnRequestRepository.save(trnRequest);

        return ResponseEntity.ok(trnRequest);
    }

    @PutMapping("/trn-requests/{id}/reject")
    public ResponseEntity<?> rejectRequest(@PathVariable Long id) {
        Optional<TrnRequest> requestOpt = trnRequestRepository.findById(id);
        if (requestOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Solicitud no encontrada."));
        }
        TrnRequest trnRequest = requestOpt.get();
        trnRequest.setStatus("REJECTED");
        trnRequest.setReviewedAt(LocalDateTime.now());
        trnRequestRepository.save(trnRequest);

        return ResponseEntity.ok(trnRequest);
    }
}
