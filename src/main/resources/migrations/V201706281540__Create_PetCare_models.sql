create table PetCare (
  id bigint identity primary key,
  description nvarchar(max) default null,
  currency nvarchar(255) default null,
  text nvarchar(255) default null,
  value decimal(19,2) default null
);

create table PetCareItem (
  id bigint identity primary key,
  name nvarchar(255) not null,
  description nvarchar(max) default null,
  currency nvarchar(255) default null,
  text nvarchar(255) default null,
  value decimal(19,2) default null,
  pet_care_id bigint references PetCare
);

alter table AvailableServices add petCare_id bigint references PetCare;
