package com.codesoom.assignment.account.dto;

import com.codesoom.assignment.account.domain.Account;

import java.util.ArrayList;
import java.util.List;

/**
 * 회원 목록 일급 컬렉션
 * TODO
 * 1. List<Account> list를 받는 정적 팩토리 메서드 - from(List<Account> list)
 * 2. 회원 추가
 * 3. 회원 목록 추가
 * 4. 회원 조회
 * 5. 회원 전체 조회
 * 6. 회원 삭제
 * 7. 회원 목록 empty check
 * 8. 회원 목록 사이즈
 */
public class AccountList {
    private final List<Account> store = new ArrayList<>();

    private AccountList(){}

    public static AccountList newInstance(){
        return new AccountList();
    }
}
