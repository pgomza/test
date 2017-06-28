package com.horeca.migrations;

import com.horeca.site.models.hotel.services.bar.BarCategory;
import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wojt on 6/5/17.
 */
public class V201706051905__Store_bar_categories_in_list implements SpringJdbcMigration {

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        jdbcTemplate.execute("alter table BarCategory add category_order int default null");

        List<Long> bars = jdbcTemplate.queryForList("select id from Bar", Long.class);
        Map<Long, List<BarCategory>> barToCategories = new HashMap<>();

        for (Long barId : bars) {
            List<Map<Long, BarCategory.Category>> categories = jdbcTemplate.query("select * from BarCategory where bar_id = ?",
                    (rs, rowNum) -> {
                        Map<Long, BarCategory.Category> pair = new HashMap<>();
                        Long id = rs.getLong("id");
                        BarCategory.Category category = BarCategory.Category.valueOf(rs.getString("category"));
                        pair.put(id, category);
                        return pair;
                    }, barId);

            // DRINK category should come first
            int order = 0;
            for (Map<Long, BarCategory.Category> category : categories) {
                Map.Entry<Long, BarCategory.Category> entry = category.entrySet().stream().findFirst().get();

                if (entry.getValue() == BarCategory.Category.DRINK) {
                    jdbcTemplate.update("update BarCategory set category_order = 0 where id = ?", entry.getKey());
                    order++;
                }
            }

            for (Map<Long, BarCategory.Category> category : categories) {
                Map.Entry<Long, BarCategory.Category> entry = category.entrySet().stream().findFirst().get();

                if (entry.getValue() != BarCategory.Category.DRINK) {
                    jdbcTemplate.update("update BarCategory set category_order = ? where id = ?", order,
                            entry.getKey());
                    order++;
                }
            }
        }
    }
}
