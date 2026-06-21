package com.trainshier.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.trainshier.entity.Category;
import com.trainshier.service.CategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * @param category category data
     * @return category
     */
    @PostMapping
    public Category save(
            @RequestBody Category category) {

        return categoryService.save(category);
    }

    /**
     * @return categories
     */
    @GetMapping
    public List<Category> findAll() {

        return categoryService.findAll();
    }
}