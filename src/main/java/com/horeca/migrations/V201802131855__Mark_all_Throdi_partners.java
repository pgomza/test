package com.horeca.migrations;

import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
    make sure each hotel that has at least one user account associated with it is marked as a partner
 */
public class V201802131855__Mark_all_Throdi_partners implements SpringJdbcMigration {

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) {
        List<Long> listOfHotelsWithUserAccounts = jdbcTemplate.queryForList(
                "select hotelId from UserAccount", Long.class
        );
        Set<Long> hotelsWithUserAccounts = new HashSet<>(listOfHotelsWithUserAccounts);
        for (Long hotelId : hotelsWithUserAccounts) {
            jdbcTemplate.update("UPDATE Hotel SET isThrodiPartner = 1 WHERE id = ?", hotelId);
        }
    }
}
