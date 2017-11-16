package com.horeca.site.models.accounts;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.Instant;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

@MappedSuperclass
public abstract class AccountPending {

    @Id
    @Email
    private String email;

    @NotEmpty
    private String password;

    @NotEmpty
    private String secret;

    @NotNull
    @LastModifiedDate
    private Long lastModifiedAt;

    AccountPending() {}

    public AccountPending(String email, String password, String secret) {
        this.email = email;
        this.password = password;
        this.secret = secret;
        this.lastModifiedAt = Instant.now().getMillis();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Long getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setLastModifiedAt(Long lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }
}
