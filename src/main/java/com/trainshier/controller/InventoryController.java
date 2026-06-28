package com.trainshier.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.trainshier.dto.InventoryResponseDTO;
import com.trainshier.service.InventoryService;
import com.trainshier.entity.InventorySimulation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    /**
     * @param sessionId session id
     * @return inventory
     */
    @GetMapping("/{accessId}")
    public List<InventorySimulation> findByAccess(
            @PathVariable Long accessId) {

        return inventoryService.findByAccess(accessId);
    }
}