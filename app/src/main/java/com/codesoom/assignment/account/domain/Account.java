package com.codesoom.assignment.account.domain;

import com.codesoom.assignment.account.providers.Identifier;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

/**
 * 회원 정보
 */
@Entity
@Getter
@Builder
@NoArgsConstructor
public class Account implements Identifier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String email;

    @NotBlank
    private String name;

    @NotBlank
    private String password;

    @Column(columnDefinition="BOOLEAN DEFAULT false")
    private Boolean isDeleted;

    public Account(String email, String name, String password) {
        this(null, email, name, password, false);
    }

    public Account(Long id, String email, String name, String password, Boolean isDeleted) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
        this.isDeleted = isDeleted;
    }
}
