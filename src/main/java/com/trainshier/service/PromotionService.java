package com.trainshier.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.trainshier.entity.Promotion;
import com.trainshier.repository.PromotionRepository;

import lombok.RequiredArgsConstructor;

/**
 * Promotion service.
 *
 * @param promotionRepository promotion repository
 */
@Service
@RequiredArgsConstructor
public class PromotionService {

    private final PromotionRepository promotionRepository;

    /**
     * Find active promotions.
     *
     * @return promotions
     */
    public List<Promotion> findActivePromotions() {

        return promotionRepository
                .findByActiveTrue();
    }

    /**
     * Save promotion.
     *
     * @param promotion promotion
     * @return promotion
     */
    public Promotion save(
            Promotion promotion) {

        return promotionRepository
                .save(promotion);
    }

    /**
     * Delete promotion.
     *
     * @param id promotion id
     */
    public void delete(Long id) {
        promotionRepository.deleteById(id);
    }
}