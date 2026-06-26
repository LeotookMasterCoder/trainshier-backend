package com.trainshier.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.trainshier.entity.Transaction;
import com.trainshier.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TransactionController {

    private final TransactionRepository repository;

    @GetMapping
    public List<Transaction> findAll() {

        return repository.findAll();

    }

    @PostMapping
    public Transaction save(
            @RequestBody Transaction transaction) {
        if (transaction.getDetails() != null) {
            for (com.trainshier.entity.TransactionDetail detail : transaction.getDetails()) {
                detail.setTransaction(transaction);
            }
        }
        return repository.save(transaction);
    }

}