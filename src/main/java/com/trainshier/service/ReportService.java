package com.trainshier.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.trainshier.entity.Report;
import com.trainshier.repository.ReportRepository;

import lombok.RequiredArgsConstructor;

/**
 * @param report report business logic
 */
@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;

    /**
     * @param report report data
     * @return report
     */
    public Report save(Report report) {

        report.setGeneratedAt(LocalDateTime.now());

        return reportRepository.save(report);
    }

    /**
     * @return reports
     */
    public List<Report> findAll() {
        return reportRepository.findAll();
    }

    /**
     * @param userId user id
     * @return reports
     */
    public List<Report> findByUser(Long userId) {
        return reportRepository.findByUserId(userId);
    }
}