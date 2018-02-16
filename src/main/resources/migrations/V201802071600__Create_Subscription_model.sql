CREATE TABLE Subscription (
  id BIGINT IDENTITY PRIMARY KEY,
  trialEligible BIT NOT NULL,
  hotel_id BIGINT REFERENCES Hotel
);

CREATE TABLE SubscriptionEvent (
  id BIGINT IDENTITY PRIMARY KEY,
  createdAt DATETIME2 DEFAULT GETDATE() NOT NULL,
  level INT NOT NULL,
  validityPeriod INT NOT NULL,
  subscription_id BIGINT REFERENCES Subscription
);