package com.trainshier.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trainshier.entity.TransactionPayment;

@Repository
public interface TransactionPaymentRepository
        extends JpaRepository<TransactionPayment, Long> {

    List<TransactionPayment>
    findByTransactionId(
            Long transactionId);
}