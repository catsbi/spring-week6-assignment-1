package com.codesoom.assignment.product.errors;

public class ProductNotFoundException extends RuntimeException {
    public static final String DEFAULT_MESSAGE = "해당 식별자[%d]에 해당하는 상품을 찾을 수 없었습니다.";

    public ProductNotFoundException(Long id) {
        super(String.format(DEFAULT_MESSAGE, id));
    }
}
