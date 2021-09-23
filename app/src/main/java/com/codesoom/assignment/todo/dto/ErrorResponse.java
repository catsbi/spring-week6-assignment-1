package com.codesoom.assignment.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private String message;


    public static ErrorResponse from(String message) {
        return new ErrorResponse(message);
    }
}
