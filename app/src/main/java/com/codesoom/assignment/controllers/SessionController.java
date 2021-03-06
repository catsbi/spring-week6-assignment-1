package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.dto.SessionResponseData;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Session 관련 HTTP 요청을 처리합니다.
 */

@RestController
@RequestMapping("/session")
public class SessionController {
    private AuthenticationService authenticationService;


    public SessionController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * 회원 로그인 정보에 따른 액세스토큰을 응답합니다.
     * @return 액세스토큰
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SessionResponseData login() {
        String accessToken = authenticationService.login();

        return SessionResponseData.builder()
                .accessToken(accessToken)
                .build();
    }
}
