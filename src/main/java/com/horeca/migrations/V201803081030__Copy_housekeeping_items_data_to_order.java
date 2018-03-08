package com.horeca.migrations;

import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class V201803081030__Copy_housekeeping_items_data_to_order implements SpringJdbcMigration {

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) {
        List<Map<String, Object>> orderIdToItemData = jdbcTemplate.query(
                "SELECT o.housekeeping_order_id, i.name\n" +
                        "FROM housekeeping_order_housekeeping_item o JOIN HousekeepingItem i ON o" +
                        ".housekeeping_item_id = i.id",
                (resultSet) -> {
                    List<Map<String, Object>> result = new ArrayList<>();
                    while (resultSet.next()) {
                        long orderId = resultSet.getLong(1);
                        String itemName = resultSet.getNString(2);
                        Map<String, Object> itemData = new HashMap<>();
                        itemData.put("name", itemName);
                        itemData.put("housekeeping_order_id", orderId);
                        result.add(itemData);
                    }
                    return result;
                }
        );

        for (Map<String, Object> itemData : orderIdToItemData) {
            SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
            simpleJdbcInsert
                    .withTableName("HousekeepingItemData")
                    .usingGeneratedKeyColumns("id")
                    .execute(itemData);
        }

        jdbcTemplate.execute("DROP TABLE housekeeping_order_housekeeping_item");
        jdbcTemplate.execute("DROP TABLE audit.housekeeping_order_housekeeping_item_AUD");
    }
}
