package com.horeca.migrations;

import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class V201803081410__Copy_pet_care_items_data_to_orders implements SpringJdbcMigration {

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) {
        // copy data of each referenced service item
        Map<Long, Map<String, Object>> orderIdToServiceItemData = jdbcTemplate.query(
                "SELECT o.id, i.name, i.description, i.currency, i.text, i.value " +
                        "FROM PetCareOrder o JOIN PetCareItem i ON o.item_id = i.id",
                (resultSet) -> {
                    Map<Long, Map<String, Object>> result = new HashMap<>();
                    while (resultSet.next()) {
                        long orderId = resultSet.getLong(1);
                        String itemName = resultSet.getString(2);
                        String itemDescription = resultSet.getString(3);
                        String itemPriceCurrency = resultSet.getString(4);
                        String itemPriceText = resultSet.getString(5);
                        BigDecimal itemPriceValue = resultSet.getBigDecimal(6);

                        HashMap<String, Object> serviceItemDataMap = new HashMap<>();
                        serviceItemDataMap.put("name", itemName);
                        serviceItemDataMap.put("description", itemDescription);
                        serviceItemDataMap.put("currency", itemPriceCurrency);
                        serviceItemDataMap.put("text", itemPriceText);
                        serviceItemDataMap.put("value", itemPriceValue);

                        result.put(orderId, serviceItemDataMap);
                    }
                    return result;
                }
        );

        // add a 'reference' to the new table
        jdbcTemplate.execute(
                "ALTER TABLE PetCareOrder ADD service_item_id BIGINT REFERENCES PetCareItemData"
        );

        // update each order
        for (Map.Entry<Long, Map<String, Object>> entry : orderIdToServiceItemData.entrySet()) {
            Long orderId = entry.getKey();
            Map<String, Object> serviceItemDataMap = entry.getValue();

            SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
            Number generatedId = simpleJdbcInsert
                    .withTableName("PetCareItemData")
                    .usingGeneratedKeyColumns("id")
                    .executeAndReturnKey(serviceItemDataMap);

            jdbcTemplate.update(
                    "UPDATE PetCareOrder SET service_item_id = ? where id = ?", generatedId, orderId
            );
        }

        String foreignKeyConstraint = jdbcTemplate.queryForObject(
                "SELECT " +
                        "name " +
                        "FROM sys.foreign_keys " +
                        "WHERE parent_object_id = object_id('PetCareOrder')" +
                        "   AND object_name(referenced_object_id) LIKE 'PetCareItem'",
                String.class
        );

        jdbcTemplate.execute("ALTER TABLE PetCareOrder DROP CONSTRAINT " + foreignKeyConstraint);
        jdbcTemplate.execute("ALTER TABLE PetCareOrder DROP COLUMN item_id");

        jdbcTemplate.execute("ALTER TABLE audit.PetCareOrder_AUD ADD service_item_id BIGINT");
    }
}
