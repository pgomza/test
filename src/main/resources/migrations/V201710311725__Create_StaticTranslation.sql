CREATE TABLE StaticTranslation (
  language NVARCHAR(255) PRIMARY KEY
);

CREATE TABLE StaticTranslationEntry (
  id BIGINT IDENTITY PRIMARY KEY,
  original NVARCHAR(255) NOT NULL,
  translated NVARCHAR(255) NOT NULL,
  translation_id NVARCHAR(255) REFERENCES StaticTranslation (language),
  UNIQUE (translation_id, original)
);