package com.codesoom.assignment.product.infra;

import com.codesoom.assignment.product.domain.Product;
import com.codesoom.assignment.product.domain.ProductRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;

@Primary
public interface ProductJpaRepository extends JpaRepository<Product, Long>, ProductRepository {
}
