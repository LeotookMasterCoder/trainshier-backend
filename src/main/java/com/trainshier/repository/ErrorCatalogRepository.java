package com.trainshier.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trainshier.entity.ErrorCatalog;

@Repository
public interface ErrorCatalogRepository
        extends JpaRepository<ErrorCatalog, Long> {

    Optional<ErrorCatalog>
    findByCode(String code);
}