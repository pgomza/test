package com.horeca.site.services;

import com.horeca.site.models.hotel.Hotel;
import com.horeca.site.models.hotel.services.spa.Spa;
import com.horeca.site.models.hotel.services.spa.SpaItem;
import com.horeca.site.models.hotel.services.spa.SpaView;
import com.horeca.site.models.hotel.services.spa.calendar.SpaCalendarDay;
import com.horeca.site.models.hotel.services.spa.calendar.SpaCalendarHour;
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

    public SpaView getView(Long hotelId, String preferredLangauge) {
        Hotel hotel = hotelService.get(hotelId);
        Spa spa = hotel.getAvailableServices().getSpa();
        return spa.toView(preferredLangauge, hotel.getDefaultTranslation());
    }

    public List<SpaCalendarHour> getCalendarHours(Long hotelId, Long itemId, String date) {
        Hotel hotel = hotelService.get(hotelId);
        Set<SpaItem> items = hotel.getAvailableServices().getSpa().getItems();

        List<SpaCalendarHour> hours = new ArrayList<>();
        for (SpaItem item : items) {
            if (item.getId().equals(itemId)) {
                for (SpaCalendarDay calendarDay : item.getCalendar().getDays()) {
                    if (calendarDay.getDay().equals(date))
                        hours.addAll(calendarDay.getHours());
                }
            }
        }

        return hours;
    }
}
