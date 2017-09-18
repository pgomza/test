package com.horeca.site.models.accounts;

import org.hibernate.validator.constraints.NotEmpty;

public class PasswordResetConfirmation {

    @NotEmpty
    private String newPassword;

    @NotEmpty
    private String secret;

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
