package com.trainshier.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trainshier.entity.SimulationError;

@Repository
public interface SimulationErrorRepository
        extends JpaRepository<SimulationError, Long> {

    List<SimulationError>
    findByTransactionId(
            Long transactionId);
}