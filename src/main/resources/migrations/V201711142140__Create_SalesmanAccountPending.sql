CREATE TABLE SalesmanAccountPending
(
  email NVARCHAR(255) NOT NULL PRIMARY KEY,
  password NVARCHAR(255) NOT NULL,
  secret NVARCHAR(255) NOT NULL,
  lastModifiedAt BIGINT NOT NULL
);

