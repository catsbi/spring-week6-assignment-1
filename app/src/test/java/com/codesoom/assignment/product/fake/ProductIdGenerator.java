package com.codesoom.assignment.product.fake;

/**
 * 상품 식별자를 제공한다.
 */
public class ProductIdGenerator implements IdGenerator<Long> {

    @Override
    public Long generate(Long source) {
        return source + 1;
    }
}
