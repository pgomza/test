alter table AvailableServices add spaCall_id bigint default NULL references SpaCall;
create index AvailableServices_SpaCall_index on AvailableServices(spaCall_id);