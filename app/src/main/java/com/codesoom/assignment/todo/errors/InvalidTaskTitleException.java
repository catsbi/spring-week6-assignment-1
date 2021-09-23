package com.codesoom.assignment.todo.errors;

public class InvalidTaskTitleException extends RuntimeException {
    public static final String DEFAULT_MESSAGE = "유효하지 않은 할 일 입니다.";

    public InvalidTaskTitleException() {
        super(DEFAULT_MESSAGE);
    }
}
