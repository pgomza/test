CREATE TABLE SubscriptionScheduling (
  id BIGINT PRIMARY KEY,
  lastTimestampChecked DATETIME2 NOT NULL
);

INSERT INTO SubscriptionScheduling (id, lastTimestampChecked) VALUES (1, '2018-01-01 00:00:00');