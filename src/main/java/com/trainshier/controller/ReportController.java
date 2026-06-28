package com.trainshier.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.trainshier.entity.Report;
import com.trainshier.service.ReportService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ReportController {

    private final ReportService reportService;

    /**
     * @return reports
     */
    @GetMapping
    public List<Report> findAll() {

        return reportService.findAll();
    }

    /**
     * @param userId user id
     * @return reports
     */
    @GetMapping("/user/{userId}")
    public List<Report> findByUser(
            @PathVariable Long userId) {

        return reportService.findByUser(userId);
    }

    /**
     * @param report report
     * @return saved report
     */
    @PostMapping
    public Report save(
            @RequestBody Report report) {

        return reportService.save(report);
    }
}