package com.trainshier.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.trainshier.entity.SessionAccess;
import com.trainshier.entity.SimulationSession;
import com.trainshier.repository.SessionAccessRepository;
import com.trainshier.repository.SessionRepository;

import lombok.RequiredArgsConstructor;

/**
 * @param session session business logic
 */
@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository simulationSessionRepository;
    private final SessionAccessRepository sessionAccessRepository;

    /**
     * @param session session data
     * @return session
     */
    public SimulationSession createSession(SimulationSession session) {

        session.setAccessCode(
                UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase());

        session.setCreatedAt(LocalDateTime.now());

        return simulationSessionRepository.save(session);
    }

    /**
     * @return sessions
     */
    public List<SimulationSession> findAll() {
        return simulationSessionRepository.findAll();
    }

    /**
     * @param access access data
     * @return access
     */
    public SessionAccess joinSession(SessionAccess access) {

        access.setLoginAt(LocalDateTime.now());

        return sessionAccessRepository.save(access);
    }
}