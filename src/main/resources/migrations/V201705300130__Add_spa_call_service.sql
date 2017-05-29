create table SpaCall
(
  id bigint identity primary key,
  description nvarchar(255) default NULL,
  number nvarchar(255) not null
);