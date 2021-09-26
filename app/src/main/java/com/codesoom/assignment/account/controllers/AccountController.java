package com.codesoom.assignment.account.controllers;

import com.codesoom.assignment.account.application.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 회원 정보 요청 Http Request 를 처리한다.
 * TODO
 * 회원 생성하기 - POST /user
 * 회원 수정하기 - POST /user/{id}
 * 회원 삭제하기 - DELETE /user/{id}
 */
@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;
}
