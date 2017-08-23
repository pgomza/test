ALTER TABLE AvailableServices ADD CONSTRAINT Fk_AvailableServices_PetCare FOREIGN KEY (petCare_id) REFERENCES PetCare(id);
ALTER TABLE AvailableServices ADD DEFAULT NULL FOR petCare_id;
CREATE INDEX AvailableServices_PetCare_index ON AvailableServices (petCare_id);
