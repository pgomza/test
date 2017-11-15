CREATE TABLE GuestProfileData (
  username NVARCHAR(255) NOT NULL REFERENCES GuestAccount (username),
  value NVARCHAR(255),
  name NVARCHAR(255) NOT NULL,
  PRIMARY KEY (username, name)
);

CREATE TABLE RootProfileData (
  username NVARCHAR(255) NOT NULL REFERENCES RootAccount (username),
  value NVARCHAR(255),
  name NVARCHAR(255) NOT NULL,
  PRIMARY KEY (username, name)
);

CREATE TABLE SalesmanProfileData (
  username NVARCHAR(255) NOT NULL REFERENCES SalesmanAccount (username),
  value NVARCHAR(255),
  name NVARCHAR(255) NOT NULL,
  PRIMARY KEY (username, name)
);

CREATE TABLE UserProfileData (
  username NVARCHAR(255) NOT NULL REFERENCES UserAccount (username),
  value NVARCHAR(255),
  name NVARCHAR(255) NOT NULL,
  PRIMARY KEY (username, name)
);