package com.horeca.migrations;

import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class V201707271530__Convert_order_statuses_to_strings implements SpringJdbcMigration {

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        String[] tableNames = new String[]{ "BarOrder", "BreakfastOrder", "CarParkOrder", "HousekeepingOrder",
                "PetCareOrder", "RentalOrder", "RoomServiceOrder", "SpaOrder", "TableOrderingOrder", "TaxiOrder" };

        for (String tableName : tableNames) {
            List<List<Object>> statusPairs = jdbcTemplate.query("SELECT id, status FROM " + tableName,
                    (rs, rowNum) -> {
                        List<Object> pair = new ArrayList<>();
                        Long id = rs.getLong("id");
                        Integer status = rs.getInt("status");
                        pair.add(id);
                        pair.add(status);
                        return pair;
                    });

            jdbcTemplate.execute("ALTER TABLE " + tableName + " ALTER COLUMN status NVARCHAR(255) NOT NULL");

            for (List<Object> pair : statusPairs) {
                Long id = (Long) pair.get(0);
                Integer statusInt = (Integer) pair.get(1);
                String statusString = convertStatus(statusInt);
                jdbcTemplate.update("UPDATE " + tableName + " SET status = ? WHERE id = ?", statusString, id);
            }
        }
    }

    private static String convertStatus(Integer status) {
        switch (status) {
            case 0: return "NEW";
            case 1: return "PENDING";
            case 2: return "ACCEPTED";
            case 3: return "REJECTED";
            case 4: return "COMPLETED";
            case 5: return "CANCELLED";
            default: return "NEW";
        }
    }
}
