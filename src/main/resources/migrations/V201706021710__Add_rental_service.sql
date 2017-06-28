create table Rental
(
  id bigint identity primary key,
  description nvarchar(255) default NULL,
  currency nvarchar(255) default NULL,
  text nvarchar(255) default NULL,
  value decimal(19,2) default NULL,
  fromHour varbinary(255),
  toHour varbinary(255)
);

create table RentalCategory
(
  id bigint identity primary key,
  category nvarchar(255) default NULL,
  rental_id bigint default NULL references Rental
);

create index rental_id on RentalCategory(rental_id);

create table RentalItem
(
  id bigint identity primary key,
  available bit not null,
  name nvarchar(255) not null,
  currency nvarchar(255) default NULL,
  text nvarchar(255) default NULL,
  value decimal(19,2) default NULL,
  rental_category_id bigint default NULL references RentalCategory
);

create index rental_category_id on RentalItem(rental_category_id);