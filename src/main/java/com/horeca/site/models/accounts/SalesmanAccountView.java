package com.horeca.site.models.accounts;

import java.util.List;

public class SalesmanAccountView extends AccountView {

    public SalesmanAccountView(String login, List<String> roles, boolean enabled) {
        super(Type.SALESMAN, login, roles, enabled);
    }
}
