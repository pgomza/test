create table PetCareOrder (
  id bigint identity primary key,
  status int not null,
  date varbinary(255) not null,
  item_id bigint references PetCareItem,
  orders_id_pet_care bigint references Orders
);