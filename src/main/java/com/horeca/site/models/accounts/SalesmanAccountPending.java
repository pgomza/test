package com.horeca.site.models.accounts;

import javax.persistence.Entity;

@Entity
public class SalesmanAccountPending extends AccountPending {

    SalesmanAccountPending() {}

    public SalesmanAccountPending(String email, String password, String secret) {
        super(email, password, secret);
    }
}
