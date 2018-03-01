package com.horeca.migrations;

import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class V201803011110__Copy_rental_items_data_to_orders implements SpringJdbcMigration {

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) {
        // copy data of each referenced service item
        Map<Long, Map<String, Object>> orderItemIdsToServiceItems = jdbcTemplate.query(
                "SELECT o.id, i.name, i.currency, i.text, i.value " +
                        "FROM RentalOrderItem o JOIN RentalItem i ON o.item_id = i.id",
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
                "ALTER TABLE RentalOrderItem ADD service_item_id BIGINT REFERENCES ServiceItemDataWithPrice"
        );

        // update each order item
        for (Map.Entry<Long, Map<String, Object>> entry : orderItemIdsToServiceItems.entrySet()) {
            Long orderItemId = entry.getKey();
            Map<String, Object> serviceItemDataMap = entry.getValue();

            SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
            Number generatedId = simpleJdbcInsert
                    .withTableName("ServiceItemDataWithPrice")
                    .usingGeneratedKeyColumns("id")
                    .executeAndReturnKey(serviceItemDataMap);

            jdbcTemplate.update(
                    "UPDATE RentalOrderItem SET service_item_id = ? where id = ?", generatedId, orderItemId
            );
        }

        // drop the redundant column
        List<String> defaultConstraints = jdbcTemplate.queryForList(
                "SELECT con.name " +
                        "FROM sys.tables t JOIN sys.default_constraints con ON t.object_id = con.parent_object_id " +
                        "WHERE t.name LIKE 'RentalOrderItem'",
                String.class
        );
        Optional<String> constraintName = defaultConstraints.stream().filter(c -> c.contains("item")).findFirst();

        String foreignKeyConstraint = jdbcTemplate.queryForObject(
                "SELECT " +
                        "name " +
                        "FROM sys.foreign_keys " +
                        "WHERE parent_object_id = object_id('RentalOrderItem')" +
                        "   AND object_name(referenced_object_id) LIKE 'RentalItem'",
                String.class
        );

        jdbcTemplate.execute("ALTER TABLE RentalOrderItem DROP CONSTRAINT " + constraintName.get());
        jdbcTemplate.execute("ALTER TABLE RentalOrderItem DROP CONSTRAINT " + foreignKeyConstraint);
        jdbcTemplate.execute("DROP INDEX item_id ON RentalOrderItem");
        jdbcTemplate.execute("ALTER TABLE RentalOrderItem DROP COLUMN item_id");
    }
}
