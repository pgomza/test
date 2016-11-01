package com.horeca.site.services;

import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.hotel.Hotel;
import com.horeca.site.models.hotel.services.spa.Spa;
import com.horeca.site.models.hotel.services.spa.SpaItem;
import com.horeca.site.models.hotel.services.spa.SpaView;
import com.horeca.site.models.hotel.services.spa.calendar.SpaCalendar;
import com.horeca.site.models.hotel.services.spa.calendar.SpaCalendarDay;
import com.horeca.site.models.hotel.services.spa.calendar.SpaCalendarHour;
import com.horeca.site.repositories.SpaCalendarHourRepository;
import com.horeca.site.repositories.SpaRepository;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class SpaService {

    @Autowired
    private HotelService hotelService;

    @Autowired
    private SpaRepository repository;

    @Autowired
    private SpaCalendarHourRepository calendarHourRepository;

    //TODO ultimately it won't be needed because the sent date will be already resolved to the LocalDate type
    private DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-yyyy");
    
    public Spa get(Long hotelId) {
        Hotel hotel = hotelService.get(hotelId);
        return hotel.getAvailableServices().getSpa();
    }

    public SpaView getView(Long hotelId, String preferredLanguage) {
        Spa spa = get(hotelId);
        return spa.toView(preferredLanguage, hotelService.get(hotelId).getDefaultTranslation());
    }

    public Set<SpaCalendarHour> getCalendarHours(Long hotelId, Long itemId, String date) {
        //TODO ultimately it won't be needed because the sent date will be already resolved to the LocalDate type
        LocalDate resolvedDate = formatter.parseLocalDate(date);

        Set<SpaItem> items = get(hotelId).getItems();

        Set<SpaCalendarHour> hours = new HashSet<>();
        for (SpaItem item : items) {
            if (item.getId().equals(itemId)) {
                for (SpaCalendarDay calendarDay : item.getCalendar().getDays()) {
                    if (calendarDay.getDay().equals(resolvedDate))
                        hours.addAll(calendarDay.getHours());
                }
            }
        }

        return hours;
    }
    
    public SpaCalendarHour updateCalendarHour(SpaCalendarHour calendarHour) {
       return calendarHourRepository.save(calendarHour);
    }

    public Set<SpaCalendarHour> updateCalendarHours(Long hotelId, Long itemId, String date, Set<SpaCalendarHour> hours) {
        //TODO ultimately it won't be needed because the sent date will be already resolved to the LocalDate type
        LocalDate resolvedDate = formatter.parseLocalDate(date);

        Hotel hotel = hotelService.get(hotelId);
        Spa spa = get(hotelId);

        for (SpaItem item : spa.getItems()) {
            if (item.getId().equals(itemId)) {

                boolean dateAlreadyExists = false;
                for (SpaCalendarDay calendarDay : item.getCalendar().getDays()) {
                    if (calendarDay.getDay().equals(resolvedDate)) {
                        calendarDay.setHours(hours);
                        dateAlreadyExists = true;
                        break;
                    }
                }

                if (!dateAlreadyExists) {
                    Set<SpaCalendarDay> availableDays = item.getCalendar().getDays();
                    SpaCalendarDay newDay = new SpaCalendarDay();
                    newDay.setDay(resolvedDate);
                    newDay.setHours(hours);
                    availableDays.add(newDay);
                }
            }
        }

        repository.save(spa);
        //TODO return the actual result - don't assume that the outcome is as expected
        return hours;
    }
}
