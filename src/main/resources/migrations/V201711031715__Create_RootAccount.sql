CREATE TABLE RootAccount
(
  username NVARCHAR(255) NOT NULL PRIMARY KEY,
  accountNonExpired BIT NOT NULL,
  accountNonLocked BIT NOT NULL,
  credentialsNonExpired BIT NOT NULL,
  enabled BIT NOT NULL,
  password NVARCHAR(255)
);