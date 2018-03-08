CREATE TABLE PetCareItemData (
  id BIGINT IDENTITY PRIMARY KEY,
  name NVARCHAR(255) NOT NULL,
  currency NVARCHAR(255),
  text NVARCHAR(255),
  value DECIMAL(19, 2),
  description NVARCHAR(MAX)
);

CREATE TABLE audit.PetCareItemData_AUD
(
  id        BIGINT NOT NULL,
  REV       BIGINT NOT NULL REFERENCES dbo.REVISION,
  REVTYPE   SMALLINT,
  name      NVARCHAR(255),
  currency NVARCHAR(255),
  text NVARCHAR(255),
  value DECIMAL(19, 2),
  description NVARCHAR(MAX),
  PRIMARY KEY (id, REV)
);