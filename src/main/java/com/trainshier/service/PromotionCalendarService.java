package com.trainshier.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.trainshier.entity.PromotionCalendar;
import com.trainshier.repository.PromotionCalendarRepository;

import lombok.RequiredArgsConstructor;

/**
 * @param calendar promotion calendar service
 */
@Service
@RequiredArgsConstructor
public class PromotionCalendarService {

    private final PromotionCalendarRepository repository;

    /**
     * @return calendars
     */
    public List<PromotionCalendar> findAll() {
        return repository.findAll();
    }

    /**
     * @param calendar calendar data
     * @return calendar
     */
    public PromotionCalendar save(
            PromotionCalendar calendar) {

        return repository.save(calendar);
    }
}