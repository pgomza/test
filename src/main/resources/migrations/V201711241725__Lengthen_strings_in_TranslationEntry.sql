ALTER TABLE TranslationEntry ALTER COLUMN original NVARCHAR(4000) NOT NULL;
ALTER TABLE TranslationEntry ALTER COLUMN translated NVARCHAR(4000);
ALTER TABLE TranslationEntry ADD CONSTRAINT PK_TranslationEntry PRIMARY KEY (original);