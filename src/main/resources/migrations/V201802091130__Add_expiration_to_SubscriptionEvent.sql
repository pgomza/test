ALTER TABLE SubscriptionEvent ADD expiresAt DATETIME2 DEFAULT GETDATE() NOT NULL;