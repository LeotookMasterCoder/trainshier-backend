package com.trainshier.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trainshier.entity.Report;

@Repository
public interface ReportRepository
        extends JpaRepository<Report, Long> {

    List<Report> findByUserId(
            Long userId);
}