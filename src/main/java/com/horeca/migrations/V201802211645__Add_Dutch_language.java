package com.horeca.migrations;

import com.horeca.site.models.hotel.translation.LanguageCode;
import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.horeca.site.models.hotel.translation.LanguageCode.*;

public class V201802211645__Add_Dutch_language implements SpringJdbcMigration {

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) {
        List<Long> activeHotels = jdbcTemplate.queryForList(
                "select id from Hotel where isThrodiPartner = 1", Long.class
        );

        List<String> requiredCodes = Stream.of(EN, FR, ES, NL).map(LanguageCode::name).collect(Collectors.toList());
        for (Long hotelId : activeHotels) {
            for (String code : requiredCodes) {
                int positiveIfExists = jdbcTemplate.queryForObject(
                        "select count(*) from HotelTranslation where languageCode = ? and hotel_id = ?",
                        Integer.class,
                        code, hotelId
                );
                if (positiveIfExists == 0) {
                    jdbcTemplate.update(
                            "insert into HotelTranslation (languageCode, hotel_id) values (?, ?)",
                            code, hotelId
                    );
                }
            }
        }
    }
}
