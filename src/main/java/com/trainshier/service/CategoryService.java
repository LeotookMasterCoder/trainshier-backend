package com.trainshier.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.trainshier.entity.Category;
import com.trainshier.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

/**
 * Category service.
 *
 * @param categoryRepository category repository
 */
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * Find all categories.
     *
     * @return categories
     */
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    /**
     * Find category by id.
     *
     * @param id category id
     * @return category
     */
    public Category findById(Long id) {

        return categoryRepository.findById(id)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Category not found"
                        )
                );
    }

    /**
     * Save category.
     *
     * @param category category
     * @return category
     */
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    /**
     * Delete category.
     *
     * @param id category id
     */
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }
}
