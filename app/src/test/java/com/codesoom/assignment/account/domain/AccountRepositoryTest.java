package com.codesoom.assignment.account.domain;

import com.codesoom.assignment.account.providers.ProvideValidAccountArguments;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.codesoom.assignment.account.ConstantsForAccountTest.나나;
import static com.codesoom.assignment.account.ConstantsForAccountTest.맥스틸;
import static com.codesoom.assignment.account.ConstantsForAccountTest.캣츠비;
import static com.codesoom.assignment.account.ConstantsForAccountTest.코드숨;
import static com.codesoom.assignment.account.ConstantsForAccountTest.크롱;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("AccountRepository 클래스")
@DataJpaTest
class AccountRepositoryTest {
    private List<Account> accounts;

    @BeforeEach
    void setUp() {
        accounts = accountRepository.saveAll(Arrays.asList(캣츠비, 크롱, 나나, 맥스틸, 코드숨));
    }

    @Autowired
    private AccountRepository accountRepository;

    @DisplayName("findAll 메서드는")
    @Nested
    class Describe_findAll {
        @DisplayName("저장된 회원(들)이 있을 경우")
        @Nested
        class Context_with_exists_accounts {
            @DisplayName("회원 목록을 반환한다.")
            @Test
            void findAllWithExistsAccount() {
                final List<Account> foundAccounts = accountRepository.findAll();

                assertThat(foundAccounts).hasSize(accounts.size());
                assertThat(foundAccounts).containsAll(accounts);
            }
        }

        @DisplayName("저장된 회원이 없을 경우")
        @Nested
        class Context_with_not_exists_accounts {
            @BeforeEach
            void cleanUp() {
                accountRepository.deleteAll();
            }

            @DisplayName("빈 목록을 반환한다.")
            @Test
            void findAllWithEmptyAccount() {
                final List<Account> foundAccounts = accountRepository.findAll();

                assertThat(foundAccounts).isEmpty();
            }
        }

    }

    @DisplayName("findById 메서드는")
    @Nested
    class Describe_findById {
        @DisplayName("등록된 회원의 식별자로 조회를 할 경우, 회원 정보가 담긴 Optional 객체를 반환 한다.")
        @Test
        void findByIdWithExistsAccount() {
            for (Account account : accounts) {
                final Optional<Account> foundAccountOpt = accountRepository.findById(account.getId());

                assertThat(foundAccountOpt)
                        .isPresent()
                        .contains(account);
            }
        }

        @DisplayName("등록된 회원의 식별자로 조회를 할 경우, 회원 정보가 담긴 Optional 객체를 반환 한다.")
        @ParameterizedTest
        @ValueSource(longs = {Long.MIN_VALUE, Long.MAX_VALUE, 0L, 99L, 999L, 5000L, 9999L, 123456L})
        void findByIdWithNotExistsAccount(Long invalidId) {
            final Optional<Account> foundAccountOpt = accountRepository.findById(invalidId);

            assertThat(foundAccountOpt).isEmpty();
        }
    }

    @DisplayName("saveAll 메서드는")
    @Nested
    class Describe_saveAll {
        @BeforeEach
        void cleanUp() {
            accountRepository.deleteAll();
        }

        @DisplayName("회원 목록을 저장한다.")
        @Test
        void saveAllAccounts() {
            final List<Account> accounts = Arrays.asList(캣츠비, 크롱, 나나, 맥스틸, 코드숨);

            final List<Account> savedAccounts = accountRepository.saveAll(accounts);

            assertThat(savedAccounts).hasSize(accounts.size());
        }
    }

    @DisplayName("deleteAll 메서드는")
    @Nested
    class Describe_deleteAll {
        @DisplayName("회원 목록을 삭제한다.")
        @Test
        void deleteAllAccount() {
            accountRepository.deleteAll();

            assertThat(accountRepository.findAll()).isEmpty();
        }
    }

    @DisplayName("delete 메서드는")
    @Nested
    class Describe_delete {
        @DisplayName("회원 정보를 삭제한다.")
        @Test
        void deleteAccount() {
            final List<Account> foundAccounts = accountRepository.findAll();
            int initSize = foundAccounts.size();

            for (Account account : accounts) {
                accountRepository.delete(account);
                initSize--;

                assertThat(accountRepository.findAll()).hasSize(initSize);
            }

            assertThat(accountRepository.findAll()).isEmpty();
        }
    }

    @DisplayName("save 메서드는")
    @Nested
    class Describe_save {
        @DisplayName("등록되지 않은 회원의 경우, 새로 회원을 등록한다.")
        @ParameterizedTest
        @ArgumentsSource(ProvideValidAccountArguments.class)
        void saveWithNotExistsAccount(String email, String name, String password) {
            int initSize = accountRepository.findAll()
                    .size();

            final Account newAccount = Account.builder()
                    .name(name)
                    .email(email)
                    .password(password)
                    .build();

            final Account savedAccount = accountRepository.save(newAccount);

            assertThat(savedAccount.getName()).isEqualTo(name);
            assertThat(savedAccount.getEmail()).isEqualTo(email);
            assertThat(savedAccount.getPassword()).isEqualTo(password);

            assertThat(accountRepository.findAll()).hasSize(initSize + 1);
        }

        @DisplayName("등록된 회원의 경우, 회원 정보를 덮어쓰기 한다.")
        @Test
        void saveWithExistsAccount() {
            final String nameForTest = "변경된회원이름TEST";
            int initSize = accounts.size();

            for (Account account : accounts) {
                ReflectionTestUtils.setField(account, "name", nameForTest);
            }

            assertThat(accountRepository.findAll()).hasSize(initSize);
        }
    }
}
