package com.horeca.migrations;

import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.util.Collections;
import java.util.List;

public class V201802121715__Initialize_AvailableServices_in_active_hotels implements SpringJdbcMigration {

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        List<Long> uninitializedActiveHotels = jdbcTemplate.queryForList(
                "select id from Hotel where isThrodiPartner = 1 and availableServices_id is null", Long.class
        );
        for (Long hotelId : uninitializedActiveHotels) {
            SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
            Number generatedId = simpleJdbcInsert
                    .withTableName("AvailableServices")
                    .usingGeneratedKeyColumns("id")
                    .executeAndReturnKey(Collections.emptyMap());
            jdbcTemplate.update("update Hotel set availableServices_id = ? where id = ?", generatedId, hotelId);
        }
    }
}
