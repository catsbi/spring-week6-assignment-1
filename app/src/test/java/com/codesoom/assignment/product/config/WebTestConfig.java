package com.codesoom.assignment.product.config;

import com.codesoom.assignment.product.application.ProductService;
import com.codesoom.assignment.product.domain.ProductRepository;
import com.codesoom.assignment.product.fake.ProductInMemoryRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class WebTestConfig {

    @Bean
    public ProductService productService() {
        return new ProductService(productRepository());
    }

    @Bean
    public ProductRepository productRepository() {
        return ProductInMemoryRepository.getInstance();
    }
}
