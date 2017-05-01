create table TableOrdering
(
  id bigint not null identity(14, 1) primary key,
  description nvarchar(255) default NULL
)