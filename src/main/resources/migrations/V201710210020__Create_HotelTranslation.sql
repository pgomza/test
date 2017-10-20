CREATE TABLE HotelTranslation (
  id BIGINT IDENTITY PRIMARY KEY,
  languageCode NVARCHAR(255) NOT NULL,
  hotel_id BIGINT REFERENCES Hotel (id),
  UNIQUE (languageCode, hotel_id)
);

CREATE TABLE TranslationEntry (
  original NVARCHAR(255) PRIMARY KEY,
  translated NVARCHAR(255) NOT NULL,
  hotel_translation_id BIGINT REFERENCES HotelTranslation (id)
);