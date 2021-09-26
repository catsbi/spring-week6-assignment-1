package com.codesoom.assignment.account.application;

import com.codesoom.assignment.account.domain.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 회원 정보를 관리한다.
 * TODO
 * 1. 회원 생성하기  createAccount(EntitySupplier supplier)
 * 2. 회원 수정하기  updateAccount(Long id, EntitySupplier supplier)
 * 3. 회원 삭제하기  deleteAccount(Long id);
 */
@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
}
