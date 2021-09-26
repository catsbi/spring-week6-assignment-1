package com.codesoom.assignment.account.dto;

import com.codesoom.assignment.account.domain.Account;
import com.codesoom.assignment.common.convertors.EntitySupplier;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Builder
public class AccountUpdateData implements EntitySupplier<Account> {

    @NotNull
    private Long id;

    @NotBlank
    private String email;

    @NotBlank
    private String name;

    @NotBlank
    private String password;

    public AccountUpdateData() {
    }

    public AccountUpdateData(Long id, String email, String name, String password) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
    }

    @Override
    public Account toEntity() {
        return Account.builder()
                .name(name)
                .email(email)
                .password(password)
                .build();
    }


}
