package com.trainshier.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trainshier.entity.PromotionCalendar;

@Repository
public interface PromotionCalendarRepository
        extends JpaRepository<PromotionCalendar, Long> {

    List<PromotionCalendar>
    findByStartDateLessThanEqualAndEndDateGreaterThanEqual(
            LocalDate currentDate,
            LocalDate currentDate2);
}