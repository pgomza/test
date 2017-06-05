package com.horeca.migrations;

import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class V201706051840__Change_bar_categories implements SpringJdbcMigration {

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        List<Long> bars = jdbcTemplate.queryForList("select id from Bar", Long.class);

        Map<Long, List<Long>> barToCategories = new HashMap<>();
        Map<Long, List<Long>> barToAllItems = new HashMap<>();

        for (Long barId : bars) {
            List<Long> categories = jdbcTemplate.queryForList("select id from BarCategory where bar_id = ?",
                    Long.class, barId);
            barToCategories.put(barId, categories);

            for (Long categoryId : categories) {
                List<Long> categoryItemsIds = jdbcTemplate.queryForList("SELECT id FROM BarItem WHERE bar_category_id = ?", Long.class,
                        categoryId);
                List<Long> barItems = barToAllItems.getOrDefault(barId, new ArrayList<>());
                barItems.addAll(categoryItemsIds);
                barToAllItems.put(barId, barItems);
            }
        }

        for (Long barId : bars) {
            List<Long> categoryIds = barToCategories.get(barId);
            if (categoryIds != null) {
                Long firstCategoryId = categoryIds.get(0);
                for (Long itemId : barToAllItems.get(barId)) {
                    jdbcTemplate.update("update BarItem set bar_category_id = ? where id = ?", firstCategoryId, itemId);
                }

                for (int i = 1; i < categoryIds.size(); i++) {
                    jdbcTemplate.update("delete BarCategory where id = ?", categoryIds.get(i));
                }

                jdbcTemplate.update("update BarCategory set category = 'DRINK' where id = ?", firstCategoryId);
            }
        }
    }
}
