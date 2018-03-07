package com.horeca.migrations;

import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.math.BigDecimal;
import java.util.*;

public class V201803052245__Copy_service_items_data_to_orders implements SpringJdbcMigration {

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) {
        /*
            apply this migration to RoomService, Bar and Breakfast since the item's model
            is the same in all these services
         */
        Map<String, String> orderTableToServiceTable = new HashMap<>();
        orderTableToServiceTable.put("RoomServiceOrderItem", "RoomServiceItem");
        orderTableToServiceTable.put("BarOrderItem", "BarItem");
        orderTableToServiceTable.put("BreakfastOrderItem", "BreakfastItem");

        for (Map.Entry<String, String> entry: orderTableToServiceTable.entrySet()) {
            String orderItemTable = entry.getKey();
            String itemTable = entry.getValue();

            // copy data of each referenced service item
            Map<Long, Map<String, Object>> orderItemIdsToServiceItems = jdbcTemplate.query(
                    "SELECT o.id, i.name, i.currency, i.text, i.value FROM " +
                            orderItemTable + " o JOIN " + itemTable + " i ON o.item_id = i.id",
                    (resultSet) -> {
                        Map<Long, Map<String, Object>> orderItemIdToServiceItemDataMap = new HashMap<>();
                        while (resultSet.next()) {
                            long orderItemId = resultSet.getLong(1);
                            String itemName = resultSet.getString(2);
                            String itemPriceCurrency = resultSet.getString(3);
                            String itemPriceText = resultSet.getString(4);
                            BigDecimal itemPriceValue = resultSet.getBigDecimal(5);

                            HashMap<String, Object> serviceItemDataMap = new HashMap<>();
                            serviceItemDataMap.put("name", itemName);
                            serviceItemDataMap.put("currency", itemPriceCurrency);
                            serviceItemDataMap.put("text", itemPriceText);
                            serviceItemDataMap.put("value", itemPriceValue);

                            orderItemIdToServiceItemDataMap.put(orderItemId, serviceItemDataMap);
                        }
                        return orderItemIdToServiceItemDataMap;
                    }
            );

            // add a 'reference' to the new table
            jdbcTemplate.execute(
                    "ALTER TABLE " + orderItemTable + " ADD service_item_id BIGINT REFERENCES ServiceItemDataWithPrice"
            );

            // update each order item
            for (Map.Entry<Long, Map<String, Object>> orderItemIdToServiceItem : orderItemIdsToServiceItems.entrySet()) {
                Long orderItemId = orderItemIdToServiceItem.getKey();
                Map<String, Object> serviceItemDataMap = orderItemIdToServiceItem.getValue();

                SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
                Number generatedId = simpleJdbcInsert
                        .withTableName("ServiceItemDataWithPrice")
                        .usingGeneratedKeyColumns("id")
                        .executeAndReturnKey(serviceItemDataMap);

                jdbcTemplate.update(
                        "UPDATE " + orderItemTable + " SET service_item_id = ? where id = ?", generatedId, orderItemId
                );
            }

            // drop the redundant column
            List<String> defaultConstraints = jdbcTemplate.queryForList(
                    String.format("SELECT con.name " +
                            "FROM sys.tables t JOIN sys.default_constraints con ON t.object_id = con.parent_object_id " +
                            "WHERE t.name LIKE '%s'", orderItemTable),
                    String.class
            );
            Optional<String> constraintName = defaultConstraints.stream().filter(c -> c.contains("item")).findFirst();

            String foreignKeyConstraint = jdbcTemplate.queryForObject(
                    String.format("SELECT " +
                            "name " +
                            "FROM sys.foreign_keys " +
                            "WHERE parent_object_id = object_id('%s')" +
                            "   AND object_name(referenced_object_id) LIKE '%s'", orderItemTable, itemTable),
                    String.class
            );

            String indexName = jdbcTemplate.query(
                    String.format("EXEC sp_helpindex '%s'", orderItemTable),
                    (resultSet) -> {
                        String result = null;
                        while (resultSet.next() && result == null) {
                            String columnName = resultSet.getString("index_keys");
                            if (Objects.equals(columnName, "item_id")) {
                                result = resultSet.getString("index_name");
                            }
                        }
                        return result;
                    }
            );

            jdbcTemplate.execute("ALTER TABLE " + orderItemTable + " DROP CONSTRAINT " + constraintName.get());
            jdbcTemplate.execute("ALTER TABLE " + orderItemTable + " DROP CONSTRAINT " + foreignKeyConstraint);
            jdbcTemplate.execute("DROP INDEX " + indexName + " ON " + orderItemTable);

            jdbcTemplate.execute("ALTER TABLE " + orderItemTable + " DROP COLUMN item_id");
            jdbcTemplate.execute("ALTER TABLE audit." + orderItemTable + "_AUD ADD service_item_id BIGINT");
        }
    }
}
