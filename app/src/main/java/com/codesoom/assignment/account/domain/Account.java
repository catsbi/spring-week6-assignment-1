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
import java.util.Objects;

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

    @Column(columnDefinition = "BOOLEAN DEFAULT false")
    private boolean deleted;

    public Account(String email, String name, String password) {
        this(null, email, name, password, false);
    }

    public Account(Long id, String email, String name, String password, Boolean isDeleted) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
        this.deleted = isDeleted;
    }

    public void update(Account account) {
        this.email = account.email;
        this.name = account.name;
        this.password = account.password;
    }

    public void delete() {
        deleted = true;
    }

    public void restore() {
        deleted = false;
    }

    public boolean isMatches(Identifier identifier) {
        return !deleted
                && email.equals(identifier.getEmail())
                && password.equals(identifier.getPassword());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Account)) {
            return false;
        }
        Account account = (Account) o;
        return deleted == account.deleted
                && Objects.equals(id, account.id)
                && Objects.equals(email, account.email)
                && Objects.equals(name, account.name)
                && Objects.equals(password, account.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, name, password, deleted);
    }
}
