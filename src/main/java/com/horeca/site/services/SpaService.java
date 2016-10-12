package com.horeca.site.services;

import com.horeca.site.models.hotel.Hotel;
import com.horeca.site.models.hotel.services.spa.Spa;
import com.horeca.site.models.hotel.services.spa.SpaItem;
import com.horeca.site.models.hotel.services.spa.SpaView;
import com.horeca.site.models.hotel.services.spa.calendar.SpaCalendarDay;
import com.horeca.site.models.hotel.services.spa.calendar.SpaCalendarHour;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class SpaService {

    @Autowired
    private HotelService hotelService;

    //TODO ultimately it won't be needed because the sent date will be already resolved to the LocalDate type
    private DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-yyyy");

    public SpaView getView(Long hotelId, String preferredLanguage) {
        Hotel hotel = hotelService.get(hotelId);
        Spa spa = hotel.getAvailableServices().getSpa();
        return spa.toView(preferredLanguage, hotel.getDefaultTranslation());
    }

    public List<SpaCalendarHour> getCalendarHours(Long hotelId, Long itemId, String date) {
        //TODO ultimately it won't be needed because the sent date will be already resolved to the LocalDate type
        LocalDate resolvedDate = formatter.parseLocalDate(date);

        Hotel hotel = hotelService.get(hotelId);
        Set<SpaItem> items = hotel.getAvailableServices().getSpa().getItems();

        List<SpaCalendarHour> hours = new ArrayList<>();
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
}
