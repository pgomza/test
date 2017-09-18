package com.horeca.site.models.accounts;

import com.horeca.site.security.models.UserAccount;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Audited
public class PasswordResetPending {

    @Id
    private String username;

    @OneToOne
    @MapsId
    @JoinColumn(name = "username")
    private UserAccount account;

    @NotEmpty
    private String secret;

    @NotNull
    private Long expirationTimestamp;

    PasswordResetPending() {
    }

    public PasswordResetPending(UserAccount account, String secret, Long expirationTimestamp) {
        this.account = account;
        this.secret = secret;
        this.expirationTimestamp = expirationTimestamp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserAccount getAccount() {
        return account;
    }

    public void setAccount(UserAccount account) {
        this.account = account;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Long getExpirationTimestamp() {
        return expirationTimestamp;
    }

    public void setExpirationTimestamp(Long expirationTimestamp) {
        this.expirationTimestamp = expirationTimestamp;
    }
}
