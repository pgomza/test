ALTER TABLE Breakfast ADD available BIT NOT NULL DEFAULT 1;
ALTER TABLE audit.Breakfast_AUD ADD available BIT;

ALTER TABLE CarPark ADD available BIT NOT NULL DEFAULT 1;
ALTER TABLE audit.CarPark_AUD ADD available BIT;

ALTER TABLE Housekeeping ADD available BIT NOT NULL DEFAULT 1;
ALTER TABLE audit.Housekeeping_AUD ADD available BIT;

ALTER TABLE Spa ADD available BIT NOT NULL DEFAULT 1;
ALTER TABLE audit.Spa_AUD ADD available BIT;

ALTER TABLE PetCare ADD available BIT NOT NULL DEFAULT 1;
ALTER TABLE audit.PetCare_AUD ADD available BIT;

ALTER TABLE Taxi ADD available BIT NOT NULL DEFAULT 1;
ALTER TABLE audit.Taxi_AUD ADD available BIT;

ALTER TABLE RoomService ADD available BIT NOT NULL DEFAULT 1;
ALTER TABLE audit.RoomService_AUD ADD available BIT;

ALTER TABLE TableOrdering ADD available BIT NOT NULL DEFAULT 1;
ALTER TABLE audit.TableOrdering_AUD ADD available BIT;

ALTER TABLE SpaCall ADD available BIT NOT NULL DEFAULT 1;
ALTER TABLE audit.SpaCall_AUD ADD available BIT;

ALTER TABLE HairDresser ADD available BIT NOT NULL DEFAULT 1;
ALTER TABLE audit.HairDresser_AUD ADD available BIT;

ALTER TABLE Rental ADD available BIT NOT NULL DEFAULT 1;
ALTER TABLE audit.Rental_AUD ADD available BIT;

ALTER TABLE RestaurantMenu ADD available BIT NOT NULL DEFAULT 1;
ALTER TABLE audit.RestaurantMenu_AUD ADD available BIT;