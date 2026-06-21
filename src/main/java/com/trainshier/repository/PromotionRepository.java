package com.trainshier.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trainshier.entity.Promotion;

@Repository
public interface PromotionRepository
        extends JpaRepository<Promotion, Long> {

    List<Promotion> findByActiveTrue();
}