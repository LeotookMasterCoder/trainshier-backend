package com.trainshier.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.trainshier.entity.SimulationConfiguration;
import com.trainshier.repository.SimulationConfigurationRepository;

import lombok.RequiredArgsConstructor;

/**
 * @param configuration simulation configuration service
 */
@Service
@RequiredArgsConstructor
public class SimulationConfigurationService {

    private final SimulationConfigurationRepository repository;

    /**
     * @return configuration list
     */
    public List<SimulationConfiguration> findAll() {
        return repository.findAll();
    }

    /**
     * @param configuration configuration data
     * @return configuration
     */
    public SimulationConfiguration save(
            SimulationConfiguration configuration) {

        return repository.save(configuration);
    }
}