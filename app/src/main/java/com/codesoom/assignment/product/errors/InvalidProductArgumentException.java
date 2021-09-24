package com.codesoom.assignment.product.errors;

public class InvalidProductArgumentException extends RuntimeException {
    public static final String DEFAULT_MESSAGE = "[%s]은 유효하지 않은 상품 정보입니다.";

    public InvalidProductArgumentException(Object invalidArg) {
        super(String.format(DEFAULT_MESSAGE, invalidArg));
    }
}
