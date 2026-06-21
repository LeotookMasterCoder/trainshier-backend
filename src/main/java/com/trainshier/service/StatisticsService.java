package com.trainshier.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.trainshier.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final TransactionRepository transactionRepository;

    /**
     * @return statistics
     */
    public Map<String, Object> getStatistics() {

        Map<String, Object> stats =
                new HashMap<>();

        long totalSales =
                transactionRepository.count();

        stats.put(
                "totalSales",
                totalSales
        );

        return stats;
    }
}