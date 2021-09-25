package com.codesoom.assignment.product.domain;

import java.util.List;
import java.util.Optional;

/**
 * 상품 정보를 저장 한다.
 */
public interface ProductRepository {
    List<Product> findAll();

    Optional<Product> findById(Long id);

    <S extends Product> List<S> saveAll(Iterable<S> entities);

    void deleteAll();

    void delete(Product product);

    Product save(Product product);
}
