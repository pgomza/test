package com.horeca.migrations;

import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class V201812281255__Reassign_account_roles implements SpringJdbcMigration {

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        List<String> salesmanUsernames = jdbcTemplate.queryForList("SELECT username FROM SalesmanAccount", String.class);
        jdbcTemplate.execute("DELETE FROM SalesmanAccount_roles");
        for (String username : salesmanUsernames) {
            jdbcTemplate.update("INSERT INTO SalesmanAccount_roles (SalesmanAccount_username, roles) VALUES (?, " +
                    "'ROLE_SALESMAN')", username);
            jdbcTemplate.update("INSERT INTO SalesmanAccount_roles (SalesmanAccount_username, roles) VALUES (?, " +
                    "'ROLE_HOTEL_FULL')", username);
        }

        List<String> userUsernames = jdbcTemplate.queryForList("SELECT username FROM UserAccount", String.class);
        jdbcTemplate.execute("DELETE FROM UserAccount_roles");
        for (String username : userUsernames) {
            jdbcTemplate.update("INSERT INTO UserAccount_roles (UserAccount_username, roles) VALUES (?, " +
                    "'ROLE_USER')", username);
            jdbcTemplate.update("INSERT INTO UserAccount_roles (UserAccount_username, roles) VALUES (?, " +
                    "'ROLE_HOTEL_FULL')", username);
        }
    }
}
