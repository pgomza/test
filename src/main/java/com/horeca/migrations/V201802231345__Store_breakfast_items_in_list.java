package com.horeca.migrations;

import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.AbstractMap;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class V201802231345__Store_breakfast_items_in_list implements SpringJdbcMigration {

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.execute("ALTER TABLE BreakfastItem ADD item_order INT DEFAULT NULL");

        List<Map.Entry<Long, Long>> itemIdToCategoryIdList = jdbcTemplate.query(
                "SELECT id, category_id FROM BreakfastItem where category_id IS NOT NULL",
                (rs, rowNumber) -> {
                    Long id = rs.getLong("id");
                    Long categoryId = rs.getLong("category_id");
                    return new AbstractMap.SimpleEntry<>(id, categoryId);
                }
        );
        itemIdToCategoryIdList.sort(Comparator.comparing(Map.Entry::getValue));

        long lastCategoryId = -1L;
        int order = 0;
        for (Map.Entry<Long, Long> itemIdToCategoryId : itemIdToCategoryIdList) {
            Long categoryId = itemIdToCategoryId.getValue();
            if (categoryId != lastCategoryId) {
                order = 0;
                lastCategoryId = categoryId;
            } else {
                order++;
            }
            jdbcTemplate.update("UPDATE BreakfastItem SET item_order = ? WHERE id = ?", order, itemIdToCategoryId.getKey());
        }

        jdbcTemplate.execute("ALTER TABLE audit.BreakfastCategory_BreakfastItem_AUD ADD item_order INT DEFAULT NULL");
    }
}
