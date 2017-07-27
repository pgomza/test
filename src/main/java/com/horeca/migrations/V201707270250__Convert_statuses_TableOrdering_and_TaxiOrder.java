package com.horeca.migrations;

import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class V201707270250__Convert_statuses_TableOrdering_and_TaxiOrder implements SpringJdbcMigration {

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {

        /*
            MAKE SURE ALL CONSTRAINTS FOR THE 'status' COLUMN ARE DELETED BEFORE PROCEEDING
            for example: "ALTER TABLE TaxiOrder DROP CONSTRAINT DF__TaxiOrder__statu__1F5986F6;"
         */

        for (String tableName : new String[]{ "TableOrderingOrder", "TaxiOrder" }) {
            List<List<Object>> statusPairs = jdbcTemplate.query("SELECT id, status FROM " + tableName,
                    (rs, rowNum) -> {
                        List<Object> pair = new ArrayList<>();
                        Long id = rs.getLong("id");
                        String status = rs.getString("status");
                        pair.add(id);
                        pair.add(status);
                        return pair;
                    });

            jdbcTemplate.execute("ALTER TABLE " + tableName + " DROP COLUMN status");
            jdbcTemplate.execute("ALTER TABLE " + tableName + " ADD status INT NOT NULL DEFAULT 0");

            for (List<Object> pair : statusPairs) {
                Long id = (Long) pair.get(0);
                String statusString = (String) pair.get(1);
                Integer statusInt = convertStatus(statusString);
                jdbcTemplate.update("UPDATE " + tableName + " SET status = ? WHERE id = ?", statusInt, id);
            }
        }
    }

    private static Integer convertStatus(String status) {
        switch (status) {
            case "NEW": return 0;
            case "PENDING": return 1;
            case "ACCEPTED": return 2;
            case "REJECTED": return 3;
            case "COMPLETED": return 4;
            case "CANCELLED": return 5;
            default: return 0; // should not happen though
        }
    }
}
