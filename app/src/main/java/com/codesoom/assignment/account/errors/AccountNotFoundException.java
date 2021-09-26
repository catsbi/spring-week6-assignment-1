package com.codesoom.assignment.account.errors;

public class AccountNotFoundException extends RuntimeException {
    public static final String DEFAULT_MESSAGE = "해당 식별자[%d]로 회원을 찾을 수 없었습니다. ";

    public AccountNotFoundException(Long id) {
        super(String.format(DEFAULT_MESSAGE, id));
    }
}
