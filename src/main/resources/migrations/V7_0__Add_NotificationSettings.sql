create table NotificationSettings
(
  id bigint not null identity primary key,
  email nvarchar(255),
  breakfast bit not null,
  carPark bit not null,
  roomService bit not null,
  spa bit not null,
  petCare bit not null,
  taxi bit not null,
  housekeeping bit not null,
  tableOrdering bit not null
);

alter table Hotel add notificationSettings_id bigint default NULL references NotificationSettings;