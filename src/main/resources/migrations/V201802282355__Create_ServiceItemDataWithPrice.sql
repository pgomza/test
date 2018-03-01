CREATE TABLE ServiceItemDataWithPrice (
  id BIGINT IDENTITY PRIMARY KEY,
  name NVARCHAR(255) NOT NULL,
  currency NVARCHAR(255),
  text NVARCHAR(255),
  value DECIMAL(19, 2)
);

CREATE TABLE ServiceItemDataWithPrice_AUD
(
  id        BIGINT NOT NULL,
  REV       BIGINT NOT NULL REFERENCES dbo.REVISION,
  REVTYPE   SMALLINT,
  name      NVARCHAR(255),
  currency  NVARCHAR(255),
  text      NVARCHAR(255),
  value     DECIMAL(19, 2)
  PRIMARY KEY (id, REV)
);