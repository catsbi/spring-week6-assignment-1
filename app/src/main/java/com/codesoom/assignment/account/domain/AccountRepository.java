package com.codesoom.assignment.account.domain;

import java.util.List;
import java.util.Optional;

/**
 * 회원 정보를 저장한다.
 */
public interface AccountRepository {
    List<Account> findAll();

    Optional<Account> findById(Long id);

    <S extends Account> List<S> saveAll(Iterable<S> entities);

    void deleteAll();

    void delete(Account account);

    Account save(Account account);
}
