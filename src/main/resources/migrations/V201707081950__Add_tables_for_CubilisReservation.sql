CREATE TABLE CubilisCustomer(
  id BIGINT IDENTITY PRIMARY KEY,
  firstName NVARCHAR(255) NOT NULL,
  lastName NVARCHAR(255) NOT NULL,
  email NVARCHAR(255)
);

CREATE TABLE CubilisReservation(
  id BIGINT PRIMARY KEY,
  status NVARCHAR(255),
  arrival VARBINARY(255) NOT NULL,
  departure VARBINARY(255) NOT NULL,
  customer_id BIGINT REFERENCES CubilisCustomer
);