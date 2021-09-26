package com.codesoom.assignment.account.errors;

public class InvalidAccountArgumentException extends RuntimeException {
    public static final String DEFAULT_MESSAGE = "유효하지 않은 값입니다. 입력: %s";

    public InvalidAccountArgumentException(String invalidValue) {
        super(String.format(DEFAULT_MESSAGE, invalidValue));
    }
}
