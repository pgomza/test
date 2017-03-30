package com.horeca.config;

import org.hibernate.dialect.SQLServer2012Dialect;

import java.sql.Types;

public class CustomSQLServerDialect extends SQLServer2012Dialect {

    public CustomSQLServerDialect() {
        registerColumnType(Types.BIGINT, "bigint");
        registerColumnType(Types.BIT, "bit");
        registerColumnType(Types.CHAR, "nchar(1)");
        registerColumnType(Types.VARCHAR, 4000, "nvarchar($l)");
        registerColumnType(Types.VARCHAR, "nvarchar(max)");
        registerColumnType(Types.VARBINARY, 4000, "varbinary($1)");
        registerColumnType(Types.VARBINARY, "varbinary(max)");
        registerColumnType(Types.BLOB, "varbinary(max)");
        registerColumnType(Types.CLOB, "nvarchar(max)");
        registerColumnType(Types.NUMERIC, "decimal");
        registerColumnType(Types.FLOAT, "real");
    }
}
