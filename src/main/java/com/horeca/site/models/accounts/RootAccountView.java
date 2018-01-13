package com.horeca.site.models.accounts;

import java.util.List;

public class RootAccountView extends AccountView {

    public RootAccountView(String login, List<String> roles, boolean enabled) {
        super(Type.ROOT, login, roles, enabled);
    }
}
