package com.horeca.migrations;

import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Timestamp;
import java.util.List;

public class V201705231947__Add_creation_timestamp_to_Stay implements SpringJdbcMigration {

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        jdbcTemplate.execute("alter table Stay add createdAt DATETIME2");

        Timestamp databaseTimestamp = jdbcTemplate.queryForObject("select CURRENT_TIMESTAMP", Timestamp.class);

        List<String> allPins = jdbcTemplate.query("select pin from Stay", (resultSet, i) ->
                resultSet.getString("pin"));

        long i = 0;
        for (String pin : allPins) {
            Timestamp stayTimestamp = new Timestamp(databaseTimestamp.getTime() + (i * 1000)); // increase by one second
            jdbcTemplate.update("update Stay set createdAt = ? where pin = ?", stayTimestamp, pin);
            i++;
        }

        jdbcTemplate.execute("alter table Stay add DEFAULT current_timestamp for createdAt");
        jdbcTemplate.execute("alter table Stay alter COLUMN createdAt DATETIME2 not null");
    }
}
