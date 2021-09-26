package com.codesoom.assignment.account.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;

/**
 * AccountController 에서 발생하는 예외를 처리한다.
 */
@ControllerAdvice
@RestController
@Slf4j
public class AccountErrorAdvice {
}
