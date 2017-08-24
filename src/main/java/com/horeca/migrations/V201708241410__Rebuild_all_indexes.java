package com.horeca.migrations;

import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class V201708241410__Rebuild_all_indexes implements SpringJdbcMigration  {

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        String[] schemas = new String[]{ "dbo", "audit" };

        for (String schema : schemas) {
            List<String> tableNames = jdbcTemplate.queryForList(
                    "SELECT TABLE_NAME\n" +
                            "FROM INFORMATION_SCHEMA.TABLES\n" +
                            "WHERE TABLE_TYPE = 'BASE TABLE' AND TABLE_SCHEMA = '" + schema + "'", String.class);

            for (String tableName : tableNames) {
                jdbcTemplate.execute("ALTER INDEX ALL ON " + schema + "." + tableName + " REBUILD WITH " +
                        "(FILLFACTOR = 80, SORT_IN_TEMPDB = ON, STATISTICS_NORECOMPUTE  = ON)");
            }
        }
    }
}
