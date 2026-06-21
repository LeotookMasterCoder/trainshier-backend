package com.trainshier.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.trainshier.entity.InventorySimulation;
import com.trainshier.repository.InventoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public List<InventorySimulation> findByAccess(
            Long accessId) {

        return inventoryRepository
                .findByAccessId(accessId);
    }

    public InventorySimulation findInventory(
            Long accessId,
            Long productId) {

        return inventoryRepository
                .findByAccessIdAndProductId(
                        accessId,
                        productId
                )
                .orElseThrow(
                        () -> new RuntimeException(
                                "Inventory not found"
                        )
                );
    }

    public void consumeStock(
            Long accessId,
            Long productId,
            Integer quantity) {

        InventorySimulation inventory =
                findInventory(
                        accessId,
                        productId
                );

        if (
                inventory.getCurrentStock()
                        < quantity
        ) {
            throw new RuntimeException(
                    "Insufficient inventory"
            );
        }

        inventory.setCurrentStock(
                inventory.getCurrentStock()
                        - quantity
        );

        inventoryRepository.save(
                inventory
        );
    }

    public InventorySimulation save(
            InventorySimulation inventory) {

        return inventoryRepository
                .save(inventory);
    }
}