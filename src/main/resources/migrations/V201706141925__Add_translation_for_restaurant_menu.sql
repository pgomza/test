create table RestaurantMenuTranslation
(
  id bigint identity primary key,
  language nvarchar(255) not null,
  translatable_id bigint not null references RestaurantMenu,
  description nvarchar(255) not null
);

create table RestaurantMenuCategoryTranslation
(
  id bigint identity primary key,
  language nvarchar(255) not null,
  translatable_id bigint not null references RestaurantMenuCategory,
  name nvarchar(255) not null,
  category_order int default null,
  restaurant_menu_id bigint not null references RestaurantMenu
);

create table RestaurantMenuItemTranslation
(
  id bigint identity primary key,
  language nvarchar(255) not null,
  translatable_id bigint not null references RestaurantMenuItem,
  name nvarchar(255) not null,
  description nvarchar(255) default null,
  item_order int default null,
  restaurant_menu_category_id bigint not null references RestaurantMenuCategory
);
