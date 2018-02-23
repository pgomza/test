package com.horeca.migrations;

import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.AbstractMap;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class V201802231435__Store_breakfast_categories_in_list implements SpringJdbcMigration {

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.execute("ALTER TABLE BreakfastCategory ADD category_order INT DEFAULT NULL");

        List<Map.Entry<Long, Long>> categoryIdToServiceIdList = jdbcTemplate.query(
                "SELECT id, service_id FROM BreakfastCategory where service_id IS NOT NULL",
                (rs, rowNumber) -> {
                    Long id = rs.getLong("id");
                    Long serviceId = rs.getLong("service_id");
                    return new AbstractMap.SimpleEntry<>(id, serviceId);
                }
        );
        categoryIdToServiceIdList.sort(Comparator.comparing(Map.Entry::getValue));

        long lastCategoryId = -1L;
        int order = 0;
        for (Map.Entry<Long, Long> categoryIdToServiceId : categoryIdToServiceIdList) {
            Long serviceId = categoryIdToServiceId.getValue();
            if (serviceId != lastCategoryId) {
                order = 0;
                lastCategoryId = serviceId;
            } else {
                order++;
            }
            jdbcTemplate.update(
                    "UPDATE BreakfastCategory SET category_order = ? WHERE id = ?", order, categoryIdToServiceId.getKey()
            );
        }

        jdbcTemplate.execute("ALTER TABLE audit.Breakfast_BreakfastCategory_AUD ADD category_order INT DEFAULT NULL");
    }
}
