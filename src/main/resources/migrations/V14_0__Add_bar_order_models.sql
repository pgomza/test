create table BarOrder
(
  id bigint identity primary key,
  status int not null,
  currency nvarchar(255) default NULL,
  text nvarchar(255) default NULL,
  value decimal(19,2) default NULL,
  orders_id_bar bigint default NULL references Orders
);

create index orders_id_bar on BarOrder(orders_id_bar);

create table BarOrderItem
(
  id bigint identity primary key,
  count int not null,
  item_id bigint default NULL references BarItem,
  bar_order_id bigint default NULL references BarOrder
);

create index item_id on BarOrderItem(item_id);
create index bar_order_id on BarOrderItem(bar_order_id);

