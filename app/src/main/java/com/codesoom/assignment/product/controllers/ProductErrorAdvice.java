package com.codesoom.assignment.product.controllers;

import com.codesoom.assignment.common.response.ErrorResponse;
import com.codesoom.assignment.product.errors.InvalidProductArgumentException;
import com.codesoom.assignment.product.errors.ProductNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@ControllerAdvice
@RestController
public class ProductErrorAdvice {

    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleProductNotFoundException(RuntimeException e) {
        log.info("productNotFound: {}", e.getMessage());
        return ErrorResponse.from(e.getMessage());
    }

    @ExceptionHandler(InvalidProductArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidProductArgumentException(RuntimeException e) {
        log.info("productArgument is invalid: {}", e.getMessage());
        return ErrorResponse.from(e.getMessage());
    }
}
