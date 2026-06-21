package com.trainshier.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trainshier.entity.InventorySimulation;

@Repository
public interface InventoryRepository
        extends JpaRepository<InventorySimulation, Long> {

    Optional<InventorySimulation> findByAccessIdAndProductId(
            Long accessId,
            Long productId);

    List<InventorySimulation> findByAccessId(
            Long accessId);
}