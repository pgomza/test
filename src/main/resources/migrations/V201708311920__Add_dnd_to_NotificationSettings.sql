ALTER TABLE NotificationSettings ADD dnd BIT DEFAULT 0 NOT NULL;
ALTER TABLE audit.NotificationSettings_AUD ADD dnd BIT DEFAULT 0;