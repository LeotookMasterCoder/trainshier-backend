package com.trainshier.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trainshier.entity.TransactionDetail;

@Repository
public interface TransactionDetailRepository
        extends JpaRepository<TransactionDetail, Long> {

    List<TransactionDetail>
    findByTransactionId(
            Long transactionId);
}