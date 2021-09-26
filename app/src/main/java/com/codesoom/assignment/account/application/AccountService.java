package com.codesoom.assignment.account.application;

import com.codesoom.assignment.account.domain.Account;
import com.codesoom.assignment.account.domain.AccountRepository;
import com.codesoom.assignment.account.errors.AccountNotFoundException;
import com.codesoom.assignment.account.errors.InvalidAccountArgumentException;
import com.codesoom.assignment.common.convertors.EntitySupplier;
import com.codesoom.assignment.common.validators.Validators;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 회원 정보를 관리한다.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {
    private final AccountRepository accountRepository;

    /**
     * 회원 정보를 등록한다.
     *
     * @param supplier 회원 엔티티 제공 인터페이스
     * @return 저장된 회원 정보
     */
    public Account createAccount(EntitySupplier<Account> supplier) {
        final Account newAccount = supplier.toEntity();
        final List<String> validateResult = Validators.validate(newAccount);

        if (!validateResult.isEmpty()) {
            throw new InvalidAccountArgumentException(String.join(",", validateResult));
        }

        return accountRepository.save(newAccount);
    }

    /**
     * 회원 정보를 수정한다.
     *
     * @param id       수정할 회원 식별자
     * @param supplier 수정할 회원 정보 제공 인터페이스
     * @return 수정된 회원 정보
     * @throws AccountNotFoundException 회원 정보를 찾을 수 없는 경우
     */
    @Transactional
    public Account updateAccount(Long id, EntitySupplier<Account> supplier) {
        final Account foundAccount = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));

        final Account updateData = supplier.toEntity();
        final List<String> validateResult = Validators.validate(updateData);

        if (!validateResult.isEmpty()) {
            throw new InvalidAccountArgumentException(String.join(",", validateResult));
        }

        foundAccount.update(updateData);

        return foundAccount;
    }

    /**
     * 회원 정보를 삭제한다.
     *
     * @param id 삭제 할 회원 식별자
     * @return 삭제된 회원 정보
     * @throws AccountNotFoundException 회원 정보를 찾을 수 없는 경우
     */
    @Transactional
    public Account deleteAccount(Long id) {
        final Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));

        accountRepository.delete(account);

        return account;
    }
}
