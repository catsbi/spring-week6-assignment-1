package com.codesoom.assignment.todo.errors;

public class TaskNotFoundException extends RuntimeException {
    public static final String DEFAULT_MESSAGE = "해당 식별자를 가진 할 일을 찾을 수 없었습니다. 식별자: %s";

    public TaskNotFoundException(Long id) {
        super(String.format(DEFAULT_MESSAGE, id));
    }
}
