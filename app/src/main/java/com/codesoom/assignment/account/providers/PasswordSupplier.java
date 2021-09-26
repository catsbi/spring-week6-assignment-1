package com.codesoom.assignment.account.providers;

/**
 * 비밀번호를 제공한다.
 */
@FunctionalInterface
public interface PasswordSupplier {
    String getPassword();
}
