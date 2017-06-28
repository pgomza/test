alter table AvailableServices add rental_id bigint default NULL references Rental;
create index AvailableServices_Rental_index on AvailableServices(rental_id);