create table RestaurantMenu
(
  id bigint identity primary key,
  description nvarchar(255) default NULL
);

create table RestaurantMenuCategory
(
  id bigint identity primary key,
  name nvarchar(255) default NULL,
  category_order int default null,
  restaurant_menu_id bigint default NULL references Bar
);

create index restaurant_menu_id on RestaurantMenuCategory(restaurant_menu_id);

create table RestaurantMenuItem
(
  id bigint identity primary key,
  name nvarchar(255) not null,
  description nvarchar(255) not null,
  currency nvarchar(255) default NULL,
  text nvarchar(255) default NULL,
  value decimal(19,2) default NULL,
  item_order int default null,
  restaurant_menu_category_id bigint default NULL references RestaurantMenuCategory
);

create index restaurant_menu_category_id on RestaurantMenuItem(restaurant_menu_category_id);



