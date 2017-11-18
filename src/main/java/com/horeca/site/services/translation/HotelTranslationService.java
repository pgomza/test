package com.horeca.site.services.translation;

import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.hotel.Hotel;
import com.horeca.site.models.hotel.HotelView;
import com.horeca.site.models.hotel.translation.HotelTranslation;
import com.horeca.site.models.hotel.translation.HotelTranslationView;
import com.horeca.site.models.hotel.translation.LanguageCode;
import com.horeca.site.models.hotel.translation.TranslationEntry;
import com.horeca.site.repositories.translation.HotelTranslationRepository;
import com.horeca.site.services.DeepCopyService;
import com.horeca.site.services.HotelService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class HotelTranslationService {

    private static final Logger logger = Logger.getLogger(HotelTranslationService.class);

    @Autowired
    private HotelTranslationRepository repository;

    @Autowired
    private DeepCopyService deepCopyService;

    @Autowired
    private TranslationService translationService;

    @Autowired
    private HotelService hotelService;

    public <T> T translate(T object, Long hotelId, LanguageCode languageCode) {
        T translated = null;
        Optional<HotelTranslation> translationOpt = get(hotelId, languageCode);

        if (translationOpt.isPresent()) {
            Map<String, String> translationEntries = translationOpt.get().asEntryMap();
            try {
                if (object instanceof ResponseEntity) {
                    ResponseEntity<?> entity = (ResponseEntity<?>) object;
                    translated = (T) translationService.translate(entity, translationEntries);
                }
                else if (object instanceof Page) {
                    Page<?> page = (Page<?>) object;
                    translated = (T) translationService.translate(page, translationEntries);
                }
                else if (object instanceof Collection) {
                    Collection<?> collection = (Collection<?>) object;
                    translated = (T) translationService.translate(collection, translationEntries);
                }
                else {
                    translated = translationService.translate(object, translationEntries);
                }
            } catch (NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException e) {
                logger.error("Couldn't translate object: " + object);
                logger.error("Exception message: " + e.getMessage());
                logger.error("Exception cause: " + e.getCause());
            }
        }

        if (translated == null) {
            translated = deepCopyService.copy(object);
        }
        return translated;
    }

    public Page<Hotel> translateHotelPage(Page<Hotel> hotelPage, LanguageCode languageCode) {
        List<Hotel> hotels = hotelPage.getContent();
        List<Hotel> translatedHotels = hotels.stream()
                .map(hotel -> translate(hotel, hotel.getId(), languageCode))
                .collect(Collectors.toList());

        Pageable resultPageable = new PageRequest(hotelPage.getNumber(), hotelPage.getSize());
        return new PageImpl<>(translatedHotels, resultPageable, hotelPage.getTotalElements());
    }

    public Page<HotelView> translateHotelViewPage(Page<HotelView> hotelViewPage, LanguageCode languageCode) {
        List<HotelView> hotels = hotelViewPage.getContent();
        List<HotelView> translatedHotels = hotels.stream()
                .map(hotel -> translate(hotel, hotel.getId(), languageCode))
                .collect(Collectors.toList());

        Pageable resultPageable = new PageRequest(hotelViewPage.getNumber(), hotelViewPage.getSize());
        return new PageImpl<>(translatedHotels, resultPageable, hotelViewPage.getTotalElements());
    }

    private Optional<HotelTranslation> get(Long hotelId, LanguageCode languageCode) {
        HotelTranslation translation = repository.findByHotelIdAndLanguageCode(hotelId, languageCode);
        return Optional.ofNullable(translation);
    }

    public HotelTranslationView getView(Long hotelId, LanguageCode languageCode) {
        Optional<HotelTranslation> translationOpt = get(hotelId, languageCode);
        if (!translationOpt.isPresent()) {
            throw new ResourceNotFoundException();
        }
        return getView(translationOpt.get());
    }

    public List<HotelTranslationView> getAllViews(Long hotelId) {
        List<HotelTranslation> translations = repository.findByHotelId(hotelId);
        return translations.stream().map(this::getView).collect(Collectors.toList());
    }

    private HotelTranslationView getView(HotelTranslation translation) {
        Set<String> namesToTranslate;
        try {
            namesToTranslate = translationService.extractTranslatableProps(translation.getHotel());
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Cannot extract translatable properties");
        }

        Map<String, String> originalToTranslated = translation.asEntryMap();
        Set<String> translationNames = originalToTranslated.keySet();

        // collect used entries
        Set<TranslationEntry> usedEntries = new HashSet<>();
        for (String name : namesToTranslate) {
            String translated = originalToTranslated.getOrDefault(name, "");
            usedEntries.add(new TranslationEntry(name, translated));
        }

        // collect unused entries
        translationNames.removeAll(namesToTranslate);
        Set<TranslationEntry> unusedEntries = translationNames.stream()
                .map(name -> new TranslationEntry(name, originalToTranslated.get(name)))
                .collect(Collectors.toSet());

        // sort all the entries
        List<TranslationEntry> usedSorted = usedEntries.stream().sorted().collect(Collectors.toList());
        List<TranslationEntry> unusedSorted = unusedEntries.stream().sorted().collect(Collectors.toList());

        return new HotelTranslationView(translation.getLanguageCode(), usedSorted, unusedSorted);
    }

    public HotelTranslation update(Long hotelId, LanguageCode languageCode, Set<TranslationEntry> translationEntries) {
        Hotel hotel = hotelService.get(hotelId);
        HotelTranslation updatedTranslation = new HotelTranslation(languageCode, hotel, translationEntries);
        Optional<HotelTranslation> existingTranslationOpt = get(hotelId, languageCode);
        if (existingTranslationOpt.isPresent()) {
            HotelTranslation existingTranslation = existingTranslationOpt.get();
            existingTranslation.getEntries().clear();
            existingTranslation.getEntries().addAll(updatedTranslation.getEntries());
            return repository.save(existingTranslation);
        }
        return repository.save(updatedTranslation);
    }

    public HotelTranslation merge(Long hotelId, LanguageCode languageCode, TranslationEntry entry) {
        Optional<HotelTranslation> existingTranslationOpt = get(hotelId, languageCode);
        if (!existingTranslationOpt.isPresent()) {
            throw new ResourceNotFoundException();
        }

        HotelTranslation existingTranslation = existingTranslationOpt.get();
        boolean foundMatch = false;
        Iterator<TranslationEntry> iterator = existingTranslation.getEntries().iterator();
        while (iterator.hasNext() && !foundMatch) {
            TranslationEntry current = iterator.next();
            if (current.getOriginal().equals(entry.getOriginal())) {
                current.setTranslated(entry.getTranslated());
                foundMatch = true;
            }
        }

        if (!foundMatch) {
            existingTranslation.getEntries().add(entry);
        }

        return repository.save(existingTranslation);
    }

    public void delete(Long hotelId, LanguageCode languageCode) {
        Optional<HotelTranslation> existingTranslationOpt = get(hotelId, languageCode);
        if (!existingTranslationOpt.isPresent()) {
            throw new ResourceNotFoundException();
        }
        repository.delete(existingTranslationOpt.get());
    }
}
