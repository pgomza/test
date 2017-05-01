create table TableOrderingOrder
(
  id bigint not null identity primary key,
  numberOfPeople int not null,
  status nvarchar(255) not null,
  time varbinary(255) not null,
  orders_id_tableordering bigint default NULL references Orders
);

create index orders_id_tableordering on TableOrderingOrder(orders_id_tableordering);