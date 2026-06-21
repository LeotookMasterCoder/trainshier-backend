package com.trainshier.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.trainshier.entity.SimulationSession;
import com.trainshier.service.SessionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;

    /**
     * Crea una sesión.
     *
     * @param session datos de la sesión
     * @return sesión creada
     */
    @PostMapping
    public SimulationSession createSession(
            @RequestBody SimulationSession session) {

        return sessionService.createSession(session);
    }

    /**
     * Lista sesiones.
     *
     * @return sesiones
     */
    @GetMapping
    public List<SimulationSession> findAll() {

        return sessionService.findAll();
    }
}