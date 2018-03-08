CREATE TABLE HousekeepingItemData (
  id BIGINT IDENTITY PRIMARY KEY,
  name NVARCHAR(255) NOT NULL,
  housekeeping_order_id BIGINT REFERENCES HousekeepingOrder
);

CREATE TABLE audit.HousekeepingItemData_AUD
(
  id        BIGINT NOT NULL,
  REV       BIGINT NOT NULL REFERENCES dbo.REVISION,
  REVTYPE   SMALLINT,
  name      NVARCHAR(255),
  PRIMARY KEY (id, REV)
);

CREATE TABLE audit.HousekeepingOrder_HousekeepingItemData_AUD
(
  id        BIGINT NOT NULL,
  REV       BIGINT NOT NULL REFERENCES dbo.REVISION,
  REVTYPE   SMALLINT,
  housekeeping_order_id BIGINT NOT NULL,
  PRIMARY KEY (REV, housekeeping_order_id, id)
);