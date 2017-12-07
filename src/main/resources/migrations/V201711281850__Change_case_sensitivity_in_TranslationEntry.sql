DECLARE @tableName SYSNAME = 'TranslationEntry';
DECLARE @columnName SYSNAME = 'original';
DECLARE @constraintName SYSNAME;

SELECT @constraintName = (
  SELECT
    con.name
  FROM sys.tables t
    JOIN sys.key_constraints con ON t.object_id = con.parent_object_id
  WHERE t.name LIKE @tableName
);

DECLARE @constraintDrop NVARCHAR(255) = 'ALTER TABLE ' + @tableName + ' DROP CONSTRAINT ' + @constraintName;
DECLARE @columnUpdate NVARCHAR(255) = 'ALTER TABLE ' + @tableName + ' ALTER COLUMN ' + @columnName + ' NVARCHAR(4000) COLLATE SQL_Latin1_General_CP1_CS_AS NOT NULL';
DECLARE @constraintAddition NVARCHAR(255) = 'ALTER TABLE ' + @tableName + ' ADD CONSTRAINT ' + @constraintName + ' PRIMARY KEY (' + @columnName + ')';

EXEC(@constraintDrop)
EXEC(@columnUpdate)
EXEC(@constraintAddition)