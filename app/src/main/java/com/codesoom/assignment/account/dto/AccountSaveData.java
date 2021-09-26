package com.codesoom.assignment.account.dto;


import com.codesoom.assignment.account.domain.Account;
import com.codesoom.assignment.common.convertors.EntitySupplier;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
public class AccountSaveData implements EntitySupplier<Account> {

    @NotBlank
    private String email;

    @NotBlank
    private String name;

    @NotBlank
    private String password;

    public AccountSaveData() {
    }

    public AccountSaveData(String email, String name, String password) {
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
