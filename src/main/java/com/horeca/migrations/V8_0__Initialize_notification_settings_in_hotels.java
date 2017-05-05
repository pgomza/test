package com.horeca.migrations;

import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.util.*;

public class V8_0__Initialize_notification_settings_in_hotels implements SpringJdbcMigration {

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        List<Long> activeHotels = jdbcTemplate.query("select hotel_id from Stay",
                (resultSet, i) -> resultSet.getLong("hotel_id"));
        Set<Long> activeHotelsSet = new HashSet<>(activeHotels);

        Set<Long> hotelsToUpdate = new HashSet<>();
        for (Long id : activeHotelsSet) {
            Integer count = jdbcTemplate.queryForObject(
                    "select count(*) from Hotel where id = ? and notificationSettings_id is null",
                    Integer.class, id);
            if (count > 0)
                hotelsToUpdate.add(id);
        }

        for (Long hotelId : hotelsToUpdate) {
            SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
            jdbcInsert.withTableName("NotificationSettings").usingGeneratedKeyColumns("id");
            Map<String, Object> params = new HashMap<>();
            params.put("email", "");
            params.put("breakfast", false);
            params.put("carPark", false);
            params.put("roomService", false);
            params.put("spa", false);
            params.put("petCare", false);
            params.put("taxi", false);
            params.put("housekeeping", false);
            params.put("tableOrdering", false);
            Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(params));

            jdbcTemplate.update("update Hotel set notificationSettings_id = ? where id = ?", key.intValue(), hotelId);
        }
    }
}
