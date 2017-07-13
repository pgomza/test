CREATE TABLE CubilisConnectionStatus(
  id BIGINT IDENTITY PRIMARY KEY,
  status NVARCHAR(255) NOT NULL
);

ALTER TABLE Hotel ADD cubilisConnectionStatus_id BIGINT REFERENCES CubilisConnectionStatus;