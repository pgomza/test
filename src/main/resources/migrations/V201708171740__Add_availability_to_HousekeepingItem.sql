ALTER TABLE HousekeepingItem ADD available BIT NOT NULL DEFAULT 1;
ALTER TABLE audit.HousekeepingItem_AUD add available BIT NOT NULL DEFAULT 1;