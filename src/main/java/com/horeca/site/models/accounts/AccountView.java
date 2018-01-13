package com.horeca.site.models.accounts;

import java.util.List;

public abstract class AccountView {
    public enum Type { GUEST, USER, SALESMAN, ROOT }

    private Type type;
    private String login;
    private List<String> roles;
    private boolean enabled;

    public AccountView(Type type, String login, List<String> roles, boolean enabled) {
        this.type = type;
        this.login = login;
        this.roles = roles;
        this.enabled = enabled;
    }

    public Type getType() {
        return type;
    }

    public String getLogin() {
        return login;
    }

    public List<String> getRoles() {
        return roles;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
