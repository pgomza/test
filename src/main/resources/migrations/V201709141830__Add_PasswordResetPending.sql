CREATE TABLE PasswordResetPending (
  username NVARCHAR(255) NOT NULL PRIMARY KEY FOREIGN KEY REFERENCES UserAccount(username),
  secret NVARCHAR(255) NOT NULL,
  expirationTimestamp BIGINT NOT NULL
);

CREATE TABLE audit.PasswordResetPending_AUD (
  username NVARCHAR(255) NOT NULL,
  REV BIGINT NOT NULL REFERENCES dbo.REVISION,
  REVTYPE SMALLINT,
  secret NVARCHAR(255),
  expirationTimestamp BIGINT,
  PRIMARY KEY (username, REV)
);