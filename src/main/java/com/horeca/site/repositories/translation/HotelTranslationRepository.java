package com.horeca.site.repositories.translation;

import com.horeca.site.models.hotel.translation.HotelTranslation;
import com.horeca.site.models.hotel.translation.LanguageCode;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface HotelTranslationRepository extends CrudRepository<HotelTranslation, Long> {

    HotelTranslation findByHotelIdAndLanguageCode(Long hotelId, LanguageCode languageCode);

    List<HotelTranslation> findByHotelId(Long hotelId);
}