package com.trainshier.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.trainshier.entity.Product;
import com.trainshier.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

/**
 * Product service.
 *
 * @param productRepository product repository
 */
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * Find all products.
     *
     * @return products
     */
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    /**
     * Find product by id.
     *
     * @param id product id
     * @return product
     */
    public Product findById(Long id) {

        return productRepository.findById(id)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Product not found"
                        )
                );
    }

    /**
     * Find product by barcode.
     *
     * @param barcode barcode
     * @return product
     */
    public Product findByBarcode(String barcode) {

        return productRepository.findByBarcode(barcode)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Product not found"
                        )
                );
    }

    /**
     * Save product.
     *
     * @param product product
     * @return saved product
     */
    public Product save(Product product) {
        return productRepository.save(product);
    }

    /**
     * Delete product.
     *
     * @param id product id
     */
    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    /**
     * Reduce stock.
     *
     * @param productId product id
     * @param quantity quantity
     */
    public void reduceStock(
            Long productId,
            Integer quantity) {

        Product product =
                findById(productId);

        if (
                product.getStock() <
                        quantity
        ) {
            throw new RuntimeException(
                    "Insufficient stock"
            );
        }

        product.setStock(
                product.getStock() - quantity
        );

        productRepository.save(product);
    }

    /**
     * Increase stock.
     *
     * @param productId product id
     * @param quantity quantity
     */
    public void increaseStock(
            Long productId,
            Integer quantity) {

        Product product =
                findById(productId);

        product.setStock(
                product.getStock() + quantity
        );

        productRepository.save(product);
    }
}
