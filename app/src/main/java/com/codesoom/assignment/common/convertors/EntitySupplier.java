package com.codesoom.assignment.common.convertors;

/**
 * 엔티티 객체를 제공한다.
 */
public interface EntitySupplier<T> {
    T toEntity();
}
