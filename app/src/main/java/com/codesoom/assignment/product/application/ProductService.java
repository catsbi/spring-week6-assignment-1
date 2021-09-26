package com.codesoom.assignment.product.application;

import com.codesoom.assignment.common.convertors.EntitySupplier;
import com.codesoom.assignment.product.domain.Product;
import com.codesoom.assignment.product.domain.ProductList;
import com.codesoom.assignment.product.domain.ProductRepository;
import com.codesoom.assignment.product.errors.InvalidProductArgumentException;
import com.codesoom.assignment.product.errors.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 상품을 관리 한다.
 */
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;


    /**
     * 모든 상품을 조회한다.
     *
     * @return 상품 목록
     */
    public ProductList findAllProduct() {
        return ProductList.from(productRepository.findAll());
    }

    /**
     * 상품 상세 정보를 조회한다.
     *
     * @param id 조회할 상품 식별자
     * @return 상품 상세정보
     * @throws ProductNotFoundException 식별자로 상품을 찾을 수 없는 경우
     */
    public Product findProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    /**
     * 상품을 등록한다.
     *
     * @param supplier 상품 정보
     * @return 등록된 상품 정보
     * @throws InvalidProductArgumentException 등록 할 상품 정보가 유효하지 않은 경우
     */
    public Product createProduct(EntitySupplier<Product> supplier) {
        final Product product = supplier.toEntity();

        return productRepository.save(product);
    }

    /**
     * 상품 정보를 수정한다.
     *
     * @param id       수정할 상품 정보 식별자
     * @param supplier 상품 정보
     * @return 수정된 상품 정보
     * @throws ProductNotFoundException        식별자로 상품을 찾을 수 없는 경우
     * @throws InvalidProductArgumentException 수정 할 상품 정보가 유효하지 않은 경우
     */
    @Transactional
    public Product updateProduct(Long id, EntitySupplier<Product> supplier) {
        final Product product = findProduct(id);
        final Product updateDataProduct = supplier.toEntity();

        product.update(updateDataProduct);

        return product;
    }

    /**
     * 상품 정보를 삭제한다.
     *
     * @param id 삭제할 상품 정보 식별자
     * @return 삭제 된 상품 정보
     * @throws ProductNotFoundException 식별자로 상품을 찾을 수 없는 경우
     */
    public Product deleteProduct(Long id) {
        final Product product = findProduct(id);

        productRepository.delete(product);

        return product;
    }
}
