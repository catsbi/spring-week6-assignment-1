package com.codesoom.assignment.account.domain;

import com.codesoom.assignment.account.providers.ProvideInvalidAccountArguments;
import com.codesoom.assignment.account.providers.ProvideValidAccountArguments;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.test.util.ReflectionTestUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Account 클래스")
class AccountTest {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();

    @DisplayName("생성자는")
    @Nested
    class Describe_constructor {
        @DisplayName("인자가 유효할 경우, 유효성 검증기에 잡히지 않는다.")
        @ParameterizedTest
        @ArgumentsSource(ProvideValidAccountArguments.class)
        void createWithValidData(String email, String name, String password) {
            final Account account = new Account(email, name, password);

            final Set<ConstraintViolation<Account>> validateResult = validator.validate(account);

            assertThat(validateResult).isEmpty();
            assertThat(account.getName()).isEqualTo(name);
            assertThat(account.getEmail()).isEqualTo(email);
            assertThat(account.getPassword()).isEqualTo(password);
            assertThat(account.isDeleted()).isFalse();
        }

        @DisplayName("인자가 유효하지 않을 경우, 유효성 검증기에 잡힌다.")
        @ParameterizedTest
        @ArgumentsSource(ProvideInvalidAccountArguments.class)
        void createWithInvalidData(String email, String name, String password) {
            final Account account = new Account(email, name, password);

            final Set<ConstraintViolation<Account>> validateResult = validator.validate(account);

            assertThat(validateResult).isNotEmpty();
            for (ConstraintViolation<Account> violation : validateResult) {
                assertThat(violation.getMessage()).isEqualTo("공백일 수 없습니다");
            }
        }
    }

    @DisplayName("update 메서드는")
    @Nested
    class Describe_update {
        private Account originAccount;

        @BeforeEach
        void setUp() {
            originAccount = Account.builder()
                    .name("temp")
                    .email("temp@email.com")
                    .password("temp1234!")
                    .build();
        }

        @DisplayName("인자가 유효할 경우, 정보가 수정된다.")
        @ParameterizedTest
        @ArgumentsSource(ProvideValidAccountArguments.class)
        void updateAccountWithValidData(String email, String name, String password) {
            final Account updateAccount = new Account(email, name, password);

            originAccount.update(updateAccount);

            assertThat(originAccount.getEmail()).isEqualTo(email);
            assertThat(originAccount.getName()).isEqualTo(name);
            assertThat(originAccount.getPassword()).isEqualTo(password);
        }

        @DisplayName("인자가 유효하지 않을 경우, 예외를 던진다.")
        @ParameterizedTest
        @ArgumentsSource(ProvideInvalidAccountArguments.class)
        void updateAccountWithInvalidData(String email, String name, String password) {
            final Account updateAccount = new Account(email, name, password);

            originAccount.update(updateAccount);

            final Set<ConstraintViolation<Account>> validateResult = validator.validate(originAccount);

            assertThat(validateResult).isNotEmpty();
            for (ConstraintViolation<Account> violation : validateResult) {
                assertThat(violation.getMessage()).isEqualTo("공백일 수 없습니다");
            }

        }

    }

    @DisplayName("delete 메서드는")
    @Nested
    class Describe_delete {
        @DisplayName("회원을 삭제된 상태로 만든다.")
        @ParameterizedTest
        @ArgumentsSource(ProvideValidAccountArguments.class)
        void executeDeleteMethod(String email, String name, String password) {
            final Account account = new Account(email, name, password);

            assertThat(account.isDeleted()).isFalse();

            account.delete();

            assertThat(account.isDeleted()).isTrue();
        }
    }

    @DisplayName("restore 메서드는")
    @Nested
    class Describe_restore {
        @DisplayName("회원을 삭제 안 된 상태로 만든다.")
        @ParameterizedTest
        @ArgumentsSource(ProvideValidAccountArguments.class)
        void executeDeleteMethod(String email, String name, String password) {
            final Account account = new Account(email, name, password);
            ReflectionTestUtils.setField(account, "deleted", true);

            account.restore();

            assertThat(account.isDeleted()).isFalse();
        }
    }

    @DisplayName("isMatches 메서드는")
    @Nested
    class Describe_isMatches {
        private List<Account> deletedAccounts;
        private List<Account> registeredAccounts;

        @BeforeEach
        void setUp() {
            deletedAccounts = IntStream.rangeClosed(1, 50)
                    .mapToObj(index -> Account.builder()
                            .name("테스트" + index)
                            .email("testEmail" + index + "@email.com")
                            .password(UUID.randomUUID().toString())
                            .deleted(true)
                            .build())
                    .collect(Collectors.toList());

            registeredAccounts = IntStream.rangeClosed(51, 100)
                    .mapToObj(index -> Account.builder()
                            .name("테스트" + index)
                            .email("testEmail" + index + "@email.com")
                            .password(UUID.randomUUID().toString())
                            .deleted(false)
                            .build())
                    .collect(Collectors.toList());

        }

        @DisplayName("삭제된 상태일 경우, 거짓을 반환한다.")
        @Test
        void isMatchesWithAnywayFromDeletedState() {
            for (Account account : deletedAccounts) {
                assertThat(account.isMatches(account)).isFalse();
            }
        }

        @DisplayName("삭제 안 된 상태일 경우")
        @Nested
        class Context_with_deleted_false_state {
            @DisplayName("아이디와 비밀번호가 일치하면, 참을 반환한다.")
            @Test
            void isMatchesWithValidIdentifier() {
                for (Account registeredAccount : registeredAccounts) {
                    final Account matchTarget = Account.builder()
                            .email(registeredAccount.getEmail())
                            .password(registeredAccount.getPassword())
                            .build();

                    assertThat(registeredAccount.isMatches(matchTarget)).isTrue();
                }
            }

            @DisplayName("아이디와 비밀번호가 일치하지 않으면, 거짓을 반환한다.")
            @Test
            void isMatchesWithInvalidIdentifier() {
                final Account otherIdentifier = Account.builder()
                        .email("otherEmail@email.co.kr")
                        .password("otherPassword!")
                        .build();

                for (Account registeredAccount : registeredAccounts) {
                    assertThat(registeredAccount.isMatches(otherIdentifier)).isFalse();
                }

            }
        }
    }

}
