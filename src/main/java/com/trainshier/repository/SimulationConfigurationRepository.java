package com.trainshier.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trainshier.entity.SimulationConfiguration;

@Repository
public interface SimulationConfigurationRepository
        extends JpaRepository<SimulationConfiguration, Long> {

    List<SimulationConfiguration>
    findBySessionId(Long sessionId);
}