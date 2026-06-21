package com.trainshier.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.trainshier.entity.SimulationSession;
import com.trainshier.service.SimulationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/simulations")
@RequiredArgsConstructor
public class SimulationController {

    private final SimulationService simulationService;

    /**
     * Guarda una simulación.
     *
     * @param simulation simulación
     * @return simulación guardada
     */
    @PostMapping
    public SimulationSession save(
            @RequestBody SimulationSession simulation) {

        return simulationService.save(simulation);
    }

    /**
     * Lista simulaciones.
     *
     * @return simulaciones
     */
    @GetMapping
    public List<SimulationSession> findAll() {

        return simulationService.findAll();
    }

    /**
     * Busca simulación por id.
     *
     * @param id identificador
     * @return simulación
     */
    @GetMapping("/{id}")
    public SimulationSession findById(
            @PathVariable Long id) {

        return simulationService.findById(id);
    }
}