package com.horeca.site.models.accounts;

import javax.persistence.Entity;
import java.util.Collections;

@Entity
public class SalesmanAccountPending extends AccountPending {

    SalesmanAccountPending() {}

    public SalesmanAccountPending(String email, String password, String secret) {
        super(email, password, secret);
    }

    public SalesmanAccountView toView() {
        return new SalesmanAccountView(getEmail(), Collections.emptyList(), false);
    }
}
