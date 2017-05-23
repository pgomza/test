create table Bar
(
  id bigint identity primary key,
  description nvarchar(255) default NULL,
  currency nvarchar(255) default NULL,
  text nvarchar(255) default NULL,
  value decimal(19,2) default NULL
);

create table BarCategory
(
  id bigint identity primary key,
  category nvarchar(255) default NULL,
  bar_id bigint default NULL references Bar
);

create index bar_id on BarCategory(bar_id);

create table BarItem
(
  id bigint identity primary key,
  available bit not null,
  name nvarchar(255) not null,
  currency nvarchar(255) default NULL,
  text nvarchar(255) default NULL,
  value decimal(19,2) default NULL,
  bar_category_id bigint default NULL references BarCategory
);

create index bar_category_id on BarItem(bar_category_id);



