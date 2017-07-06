-- make sure all the conflicting constraints are removed at this point
alter table AvailableServices drop column petCare_id;
drop table PetCareOrder;
drop table PetCareItem;
drop table PetCareCalendarHour;
drop table PetCareCalendarDay;
drop table PetCareCalendar;
drop table PetCare;