package com.trainshier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trainshier.entity.SimulationSession;

@Repository
public interface SimulationSessionRepository
        extends JpaRepository<SimulationSession, Long> {
}