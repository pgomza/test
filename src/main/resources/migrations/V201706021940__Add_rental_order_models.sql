create table RentalOrder
(
  id bigint identity primary key,
  status int not null,
  currency nvarchar(255) default NULL,
  text nvarchar(255) default NULL,
  value decimal(19,2) default NULL,
  time varbinary(255) not null,
  createdAt datetime2 not null,
  orders_id_rental bigint default NULL references Orders
);

create index orders_id_rental on RentalOrder(orders_id_rental);

create table RentalOrderItem
(
  id bigint identity primary key,
  count int not null,
  item_id bigint default NULL references RentalItem,
  rental_order_id bigint default NULL references RentalOrder
);

create index item_id on RentalOrderItem(item_id);
create index rental_order_id on RentalOrderItem(rental_order_id);

