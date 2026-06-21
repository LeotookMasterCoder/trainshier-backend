package com.trainshier.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trainshier.entity.Invoice;

@Repository
public interface InvoiceRepository
        extends JpaRepository<Invoice, Long> {

    Optional<Invoice>
    findByInvoiceNumber(
            String invoiceNumber);

    Optional<Invoice>
    findByTransactionId(
            Long transactionId);
}