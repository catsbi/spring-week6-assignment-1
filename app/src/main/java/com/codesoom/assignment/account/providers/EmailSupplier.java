package com.codesoom.assignment.account.providers;

/**
 * 이메일을 제공한다.
 */
@FunctionalInterface
public interface EmailSupplier {
    String getEmail();
}
