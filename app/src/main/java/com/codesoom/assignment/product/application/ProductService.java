package com.codesoom.assignment.product.application;

import com.codesoom.assignment.common.convertors.EntitySupplier;
import com.codesoom.assignment.product.domain.Product;
import com.codesoom.assignment.product.domain.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * TODO 구현해야 할 기능
 * 고양이 장난감 목록 얻기    - List<Product> findAllProduct()
 * 고양이 장난감 상세 조회하기 - Product findProduct(Long id)
 * 고양이 장난감 등록하기     - Product createProduct(EntitySupplier data)
 * 고양이 장난감 수정하기     - Product updateProduct(Long id, EntitySupplier data)
 * 고양이 장난감 삭제하기     - Product deleteProduct(Long id)
 */

/**
 * 상품을 관리 한다.
 */
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;


    public List<Product> findAllProduct() {
        return null;
    }

    public Product findProduct(Long id) {
        return null;
    }

    public Product createProduct(EntitySupplier supplier) {
        return null;
    }

    public Product updateProduct(Long id, EntitySupplier supplier) {
        return null;
    }

    public Product deleteProduct(Long id) {
        return null;
    }
}
