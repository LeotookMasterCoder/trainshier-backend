package com.trainshier.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.trainshier.entity.Promotion;
import com.trainshier.service.PromotionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/promotions")
@RequiredArgsConstructor
public class PromotionController {

    private final PromotionService promotionService;

    /**
     * @param promotion promotion data
     * @return promotion
     */
    @PostMapping
    public Promotion save(
            @RequestBody Promotion promotion) {

        return promotionService.save(promotion);
    }

    /**
     * @return promotions
     */
    @GetMapping
    public List<Promotion> findAll() {

        return promotionService.findActivePromotions();
    }
}