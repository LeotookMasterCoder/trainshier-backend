package com.trainshier.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.trainshier.entity.SimulationSession;
import com.trainshier.repository.SessionRepository;

import lombok.RequiredArgsConstructor;

/**
 * @param simulation simulation business logic
 */
@Service
@RequiredArgsConstructor
public class SimulationService {

    private final SessionRepository simulationSessionRepository;

    /**
     * @param simulation simulation data
     * @return saved simulation
     */
    public SimulationSession save(SimulationSession simulation) {
        simulation.setCreatedAt(LocalDateTime.now());
        return simulationSessionRepository.save(simulation);
    }

    /**
     * @return simulation list
     */
    public List<SimulationSession> findAll() {
        return simulationSessionRepository.findAll();
    }

    /**
     * @param id simulation id
     * @return simulation
     */
    public SimulationSession findById(Long id) {
        return simulationSessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Simulation not found"));
    }

    /**
     * @param id simulation id
     */
    public void delete(Long id) {
        simulationSessionRepository.deleteById(id);
    }
}
