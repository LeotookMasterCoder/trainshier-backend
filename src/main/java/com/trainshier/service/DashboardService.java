package com.trainshier.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.trainshier.repository.ProductRepository;
import com.trainshier.repository.TransactionRepository;
import com.trainshier.repository.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * @param dashboard dashboard statistics
 */
@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final TransactionRepository transactionRepository;

    /**
     * @return dashboard statistics
     */
    public Map<String, Object> getStatistics() {

        Map<String, Object> stats = new HashMap<>();

        stats.put(
                "totalUsers",
                userRepository.count());

        stats.put(
                "totalProducts",
                productRepository.count());

        stats.put(
                "totalTransactions",
                transactionRepository.count());

        return stats;
    }
}