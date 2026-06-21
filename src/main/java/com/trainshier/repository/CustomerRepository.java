package com.trainshier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trainshier.entity.CustomerProfile;

@Repository
public interface CustomerRepository
        extends JpaRepository<CustomerProfile, Long> {
}