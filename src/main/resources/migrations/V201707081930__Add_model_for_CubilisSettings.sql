CREATE TABLE CubilisSettings(
  id BIGINT IDENTITY PRIMARY KEY,
  isEnabled BIT NOT NULL,
  isMergingEnabled BIT NOT NULL,
  login NVARCHAR(255) NOT NULL,
  password NVARCHAR(255) NOT NULL
);

ALTER TABLE Hotel ADD cubilisSettings_id BIGINT REFERENCES CubilisSettings;