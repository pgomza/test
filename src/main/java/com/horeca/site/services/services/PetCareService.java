package com.horeca.site.services.services;

import com.horeca.site.models.hotel.Hotel;
import com.horeca.site.models.hotel.services.petcare.PetCare;
import com.horeca.site.models.hotel.services.petcare.PetCareItem;
import com.horeca.site.models.hotel.services.petcare.calendar.PetCareCalendarDay;
import com.horeca.site.models.hotel.services.petcare.calendar.PetCareCalendarHour;
import com.horeca.site.repositories.services.PetCareCalendarHourRepository;
import com.horeca.site.repositories.services.PetCareItemRepository;
import com.horeca.site.repositories.services.PetCareRepository;
import com.horeca.site.services.HotelService;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class PetCareService {

    @Autowired
    private HotelService hotelService;

    @Autowired
    private PetCareRepository repository;

    @Autowired
    private PetCareItemRepository itemRepository;

    @Autowired
    private PetCareCalendarHourRepository calendarHourRepository;

    //TODO ultimately it won't be needed because the sent date will be already resolved to the LocalDate type
    private DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-yyyy");

    public PetCare get(Long hotelId) {
        Hotel hotel = hotelService.get(hotelId);
        return hotel.getAvailableServices().getPetCare();
    }

    public PetCareItem addItem(Long hotelId, PetCareItem item) {
        PetCare petCare = get(hotelId);
        PetCareItem savedItem = itemRepository.save(item);
        petCare.getItems().add(item);
        repository.save(petCare);
        return savedItem;
    }

    public PetCareItem updateItem(PetCareItem item) {
        return itemRepository.save(item);
    }

    public void deleteItem(Long itemId) {
        itemRepository.delete(itemId);
    }

    public Set<PetCareCalendarHour> getCalendarHours(Long hotelId, Long itemId, String date) {
        //TODO ultimately it won't be needed because the sent date will be already resolved to the LocalDate type
        LocalDate resolvedDate = formatter.parseLocalDate(date);

        Set<PetCareItem> items = get(hotelId).getItems();

        Set<PetCareCalendarHour> hours = new HashSet<>();
        for (PetCareItem item : items) {
            if (item.getId().equals(itemId)) {
                for (PetCareCalendarDay calendarDay : item.getCalendar().getDays()) {
                    if (calendarDay.getDay().equals(resolvedDate))
                        hours.addAll(calendarDay.getHours());
                }
            }
        }

        return hours;
    }

    public PetCareCalendarHour updateCalendarHour(PetCareCalendarHour calendarHour) {
        return calendarHourRepository.save(calendarHour);
    }

    public Set<PetCareCalendarHour> updateCalendarHours(Long hotelId, Long itemId, String date, Set<PetCareCalendarHour> hours) {
        //TODO ultimately it won't be needed because the sent date will be already resolved to the LocalDate type
        LocalDate resolvedDate = formatter.parseLocalDate(date);

        Hotel hotel = hotelService.get(hotelId);
        PetCare petCare = get(hotelId);

        for (PetCareItem item : petCare.getItems()) {
            if (item.getId().equals(itemId)) {

                boolean dateAlreadyExists = false;
                for (PetCareCalendarDay calendarDay : item.getCalendar().getDays()) {
                    if (calendarDay.getDay().equals(resolvedDate)) {
                        calendarDay.setHours(hours);
                        dateAlreadyExists = true;
                        break;
                    }
                }

                if (!dateAlreadyExists) {
                    Set<PetCareCalendarDay> availableDays = item.getCalendar().getDays();
                    PetCareCalendarDay newDay = new PetCareCalendarDay();
                    newDay.setDay(resolvedDate);
                    newDay.setHours(hours);
                    availableDays.add(newDay);
                }
            }
        }

        repository.save(petCare);
        //TODO return the actual result - don't assume that the outcome is as expected
        return hours;
    }
}
