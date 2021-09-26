package com.codesoom.assignment.common.convertors;

@FunctionalInterface
public interface ViewSupplier<S, R> {
    R supply(S source);
}
