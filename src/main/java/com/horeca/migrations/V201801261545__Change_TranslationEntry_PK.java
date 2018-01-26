package com.horeca.migrations;

import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class V201801261545__Change_TranslationEntry_PK implements SpringJdbcMigration {

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) {
        final String TABLE_NAME = "TranslationEntry";
        final String PK_CONSTRAINT_NAME = "PK_TranslationEntry";
        final String COLUMN_ORIGINAL = "original";
        final String COLUMN_TRANSLATED = "translated";
        final String COLUMN_HOTEL_TRANSLATION_ID = "hotel_translation_id";

        List<Map<String, Object>> entries = jdbcTemplate.query("select * from TranslationEntry", (resultSet, i) -> {
            String original = resultSet.getString(COLUMN_ORIGINAL);
            String translated = resultSet.getString(COLUMN_TRANSLATED);
            Long hotelTranslationId = resultSet.getLong(COLUMN_HOTEL_TRANSLATION_ID);

            Map<String, Object> row = new HashMap<>();
            row.put(COLUMN_ORIGINAL, original);
            row.put(COLUMN_TRANSLATED, translated);
            row.put(COLUMN_HOTEL_TRANSLATION_ID, hotelTranslationId);
            return row;
        });

        jdbcTemplate.execute("alter table " + TABLE_NAME + " drop constraint " + PK_CONSTRAINT_NAME);
        jdbcTemplate.execute("delete from " + TABLE_NAME);
        jdbcTemplate.execute("alter table " + TABLE_NAME + " add id bigint identity constraint " +
                PK_CONSTRAINT_NAME + " primary key");

        for (Map<String, Object> entry : entries) {
            String original = (String) entry.get(COLUMN_ORIGINAL);
            String translated = (String) entry.get(COLUMN_TRANSLATED);
            Long hotelTranslationId = (Long) entry.get(COLUMN_HOTEL_TRANSLATION_ID);

            jdbcTemplate.update("INSERT INTO TranslationEntry (original, translated, hotel_translation_id) " +
                    "VALUES (?, ?, ?)", original, translated, hotelTranslationId);
        }
    }
}
