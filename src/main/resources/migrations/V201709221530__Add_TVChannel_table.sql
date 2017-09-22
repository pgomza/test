CREATE TABLE TVChannel (
  hotel_id BIGINT NOT NULL REFERENCES Hotel (id),
  name NVARCHAR(255) NOT NULL,
  name_order INT NOT NULL,
  PRIMARY KEY (hotel_id, name_order)
);

CREATE TABLE audit.TVChannel_AUD (
  REV BIGINT NOT NULL REFERENCES dbo.REVISION,
  REVTYPE SMALLINT,
  hotel_id BIGINT NOT NULL,
  name NVARCHAR(255) NOT NULL,
  name_order INT NOT NULL,
  PRIMARY KEY (REV, hotel_id, name, name_order)
);