package com.trainshier;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.trainshier.entity.Product;
import com.trainshier.repository.ProductRepository;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
public class ProductSearchRestAssuredTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ProductRepository productRepository;

    private Product testProduct;

    @BeforeEach
    public void setUp() {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        RestAssuredMockMvc.mockMvc(mockMvc);
        
        String uniqueSuffix = java.util.UUID.randomUUID().toString().substring(0, 8);
        Product p = new Product();
        p.setName("Leche Colanta " + uniqueSuffix);
        p.setPrice(4500.0);
        p.setStock(12);
        p.setBarcode("barcode_" + uniqueSuffix);
        p.setActive(true);
        testProduct = productRepository.save(p);
    }

    @Test
    public void testFindProductByIdAndVerifyDetails() {
        given()
        .when()
            .get("/products/" + testProduct.getId())
        .then()
            .statusCode(200)
            .body("id", equalTo(testProduct.getId().intValue()))
            .body("name", equalTo(testProduct.getName()))
            .body("price", equalTo(4500.0f))
            .body("stock", equalTo(12));
    }
}
