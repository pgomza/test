ALTER TABLE Guest ALTER COLUMN identification NVARCHAR(255);
-- ALTER TABLE Guest ADD DEFAULT NULL FOR identification;
-- also, drop the 'unique' constraint on column 'identification'