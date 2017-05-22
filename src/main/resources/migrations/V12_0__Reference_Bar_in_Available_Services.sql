alter table AvailableServices add bar_id bigint default NULL references Bar;
create index AvailableServices_Bar_index on AvailableServices(bar_id);