package com.codesoom.assignment.product.fake;

/**
 * 식별자를 제공한다.
 *
 * @param <T> 식별자 타입
 */
public interface IdGenerator<T> {
    T generate(T source);
}
