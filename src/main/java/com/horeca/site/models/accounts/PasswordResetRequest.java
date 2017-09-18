package com.horeca.site.models.accounts;

import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

public class PasswordResetRequest {

    @NotEmpty
    private String login;

    @URL
    private String redirectUrl;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
}
