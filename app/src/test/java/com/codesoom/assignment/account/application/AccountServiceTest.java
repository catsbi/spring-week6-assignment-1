package com.codesoom.assignment.account.application;

import com.codesoom.assignment.account.domain.Account;
import com.codesoom.assignment.account.domain.AccountRepository;
import com.codesoom.assignment.account.dto.AccountSaveData;
import com.codesoom.assignment.account.dto.AccountUpdateData;
import com.codesoom.assignment.account.errors.AccountNotFoundException;
import com.codesoom.assignment.account.errors.InvalidAccountArgumentException;
import com.codesoom.assignment.account.providers.ProvideInvalidAccountArguments;
import com.codesoom.assignment.account.providers.ProvideValidAccountArguments;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Arrays;
import java.util.List;

import static com.codesoom.assignment.account.ConstantsForAccountTest.나나;
import static com.codesoom.assignment.account.ConstantsForAccountTest.맥스틸;
import static com.codesoom.assignment.account.ConstantsForAccountTest.캣츠비;
import static com.codesoom.assignment.account.ConstantsForAccountTest.코드숨;
import static com.codesoom.assignment.account.ConstantsForAccountTest.크롱;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("AccountService 클래스")
@DataJpaTest
class AccountServiceTest {
    @Autowired
    private AccountRepository accountRepository;

    private AccountService accountService;

    private List<Account> accounts;

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        accountService = new AccountService(accountRepository);

        accounts = accountRepository.saveAll(Arrays.asList(캣츠비, 크롱, 나나, 맥스틸, 코드숨));
    }

    @AfterEach
    void cleanUp() {
        accountRepository.deleteAll();
    }

    @DisplayName("createAccount 메서드는")
    @Nested
    class Describe_createAccount {
        @DisplayName("등록하려는 회원 정보가 유효한 경우, 저장 후 회원 정보를 반환한다.")
        @ParameterizedTest
        @ArgumentsSource(ProvideValidAccountArguments.class)
        void createAccountWithValidData(String email, String name, String password) {
            final AccountSaveData saveData = new AccountSaveData(email, name, password);

            final Account savedAccount = accountService.createAccount(saveData);

            assertThat(savedAccount.getEmail()).isEqualTo(email);
            assertThat(savedAccount.getName()).isEqualTo(name);
            assertThat(savedAccount.getPassword()).isEqualTo(password);

        }

        @DisplayName("등록하려는 회원 정보가 유효하지 않은 경우, 예외를 던진다.")
        @ParameterizedTest
        @ArgumentsSource(ProvideInvalidAccountArguments.class)
        void createAccountWithInvalidData(String email, String name, String password) {
            final AccountSaveData saveData = new AccountSaveData(email, name, password);

            assertThatThrownBy(() -> accountService.createAccount(saveData))
                    .isInstanceOf(InvalidAccountArgumentException.class);
        }
    }

    @DisplayName("updateAccount 메서드는")
    @Nested
    class Describe_updateAccount {
        @DisplayName("등록 된 회원의 정보를 수정 하려는 경우")
        @Nested
        class Context_with_exists_account {
            @DisplayName("수정 할 정보가 유효한 경우, 정보가 수정되고 회원 정보를 반환 한다.")
            @ParameterizedTest
            @ArgumentsSource(ProvideValidAccountArguments.class)
            void updateAccountWithValidData(String email, String name, String password) {
                final Account targetAccount = accounts.get(0);
                final AccountUpdateData updateData = new AccountUpdateData(targetAccount.getId(), email, name, password);

                final Account updatedAccount = accountService.updateAccount(targetAccount.getId(), updateData);

                assertThat(updatedAccount.getId()).isEqualTo(targetAccount.getId());
                assertThat(updatedAccount.getName()).isEqualTo(name);
                assertThat(updatedAccount.getEmail()).isEqualTo(email);
                assertThat(updatedAccount.getPassword()).isEqualTo(password);


            }

            @DisplayName("수정 할 정보가 유효하지 않은 경우, 예외를 던진다.")
            @ParameterizedTest
            @ArgumentsSource(ProvideInvalidAccountArguments.class)
            void updateAccountWithInvalidData(String email, String name, String password) {
                final Account targetAccount = accounts.get(0);
                final AccountUpdateData updateData = new AccountUpdateData(targetAccount.getId(), email, name, password);

                assertThatThrownBy(() -> accountService.updateAccount(targetAccount.getId(), updateData))
                        .isInstanceOf(InvalidAccountArgumentException.class);
            }

        }

        @DisplayName("등록 안 된 회원의 정보를 수정 하려는 경우, 예외를 던진다.")
        @ParameterizedTest
        @ArgumentsSource(ProvideValidAccountArguments.class)
        void updateAccountWithNotExistsIdAndValidData(String email, String name, String password) {
            final AccountUpdateData updateData = new AccountUpdateData(Long.MAX_VALUE, email, name, password);

            assertThatThrownBy(() -> accountService.updateAccount(Long.MAX_VALUE, updateData))
                    .isInstanceOf(AccountNotFoundException.class)
                    .hasMessage(String.format(AccountNotFoundException.DEFAULT_MESSAGE, Long.MAX_VALUE));
        }


    }

    @DisplayName("deleteAccount 메서드는")
    @Nested
    class Describe_deleteAccount {
        @DisplayName("등록된 회원의 정보를 삭제하려는 경우, 회원 정보 삭제 후 삭제 된 정보를 반환한다.")
        @Test
        void deleteAccountWithExistsId() {
            for (Account account : accounts) {
                accountService.deleteAccount(account.getId());
            }

            assertThat(accountRepository.findAll()).isEmpty();
        }

        @DisplayName("등록되지 않은 회원의 정보를 삭제하려는 경우, 예외를 던진다.")
        @ParameterizedTest
        @ValueSource(longs = {Long.MAX_VALUE, Long.MIN_VALUE, 0L, 999L, 1999L, 1199L, 99999L})
        void deleteAccountWithNotExistsId(Long invalidId) {
            assertThatThrownBy(() -> accountService.deleteAccount(invalidId))
                    .isInstanceOf(AccountNotFoundException.class)
                    .hasMessage(String.format(AccountNotFoundException.DEFAULT_MESSAGE, invalidId));
        }

    }
}
