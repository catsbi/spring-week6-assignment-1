package com.codesoom.assignment.account.infra;

import com.codesoom.assignment.account.domain.Account;
import com.codesoom.assignment.account.domain.AccountRepository;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 회원 정보를 저장한다.
 */
public interface AccountJpaRepository extends JpaRepository<Account, Long> , AccountRepository {
}
