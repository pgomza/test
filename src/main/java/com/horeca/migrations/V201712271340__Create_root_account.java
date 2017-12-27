package com.horeca.migrations;

import com.horeca.site.security.services.PasswordHashingService;
import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class V201712271340__Create_root_account implements SpringJdbcMigration {

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        String hashedPassword = PasswordHashingService.getHashedFromPlain("4dWpGKdGKX");
        jdbcTemplate.update("INSERT INTO RootAccount (username, accountNonExpired, accountNonLocked," +
                "credentialsNonExpired, enabled, password) VALUES ('root', 1, 1, 1, 1, ?)", hashedPassword);
    }
}
