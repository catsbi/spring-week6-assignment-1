package com.codesoom.assignment.product.application;

import com.codesoom.assignment.common.convertors.EntitySupplier;
import com.codesoom.assignment.product.domain.Product;
import com.codesoom.assignment.product.domain.ProductRepository;
import com.codesoom.assignment.product.errors.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 상품을 관리 한다.
 */
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;


    public List<Product> findAllProduct() {
        return productRepository.findAll();
    }

    public Product findProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    public Product createProduct(EntitySupplier<Product> supplier) {
        final Product product = supplier.toEntity();

        return productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(Long id, EntitySupplier<Product> supplier) {
        final Product product = findProduct(id);
        final Product updateDataProduct = supplier.toEntity();

        product.update(updateDataProduct);

        return product;
    }

    public Product deleteProduct(Long id) {
        final Product product = findProduct(id);

        productRepository.delete(product);

        return product;
    }
}
