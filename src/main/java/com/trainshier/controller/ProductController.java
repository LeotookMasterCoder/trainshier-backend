package com.trainshier.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.trainshier.entity.Product;
import com.trainshier.service.ProductService;

import lombok.RequiredArgsConstructor;

/**
 * Product controller.
 */
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductService productService;

    /**
     * Saves or updates a product.
     *
     * @param product product data
     * @return saved product
     */
    @PostMapping
    public Product save(
            @RequestBody Product product) {

        return productService.save(product);
    }

    /**
     * Returns all products.
     *
     * @return products list
     */
    @GetMapping
    public List<Product> findAll() {

        return productService.findAll();
    }

    /**
     * Finds a product by id.
     *
     * @param id product id
     * @return product
     */
    @GetMapping("/{id}")
    public Product findById(
            @PathVariable Long id) {

        return productService.findById(id);
    }

    /**
     * Deletes a product by id.
     *
     * @param id product id
     */
    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable Long id) {

        productService.delete(id);
    }
}