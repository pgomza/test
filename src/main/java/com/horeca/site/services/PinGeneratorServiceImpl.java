package com.horeca.site.services;

import com.horeca.site.models.AvailablePin;
import com.horeca.site.models.Stay;
import com.horeca.site.repositories.AvailablePinRepository;
import com.horeca.site.repositories.StayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
public class PinGeneratorServiceImpl implements PinGeneratorService {

    @Value("${availablePins.checkOnStartup}")
    private boolean shouldCheckOnStartup;

    @Autowired
    private AvailablePinRepository pinRepository;

    @Autowired
    private StayRepository stayRepository;

    @PostConstruct
    @Transactional
    private void checkOnStartup() {
        if (shouldCheckOnStartup) {
            Iterable<Stay> allStays = stayRepository.findAll();
            Set<String> takenPins = new HashSet<>();
            for (Stay stay : allStays) {
                takenPins.add(stay.getPin());
            }

            pinRepository.deleteAll();
            List<AvailablePin> availablePins = new ArrayList<>();
            for (int i = 1000; i < 2000; i++) {
                String currentPin = String.format("%04d", i);
                if (!takenPins.contains(currentPin))
                    availablePins.add(new AvailablePin(currentPin));
            }

            Collections.shuffle(availablePins);
            pinRepository.save(availablePins);
        }
    }

    @Override
    @Transactional
    public String generatePin() {
        long count = pinRepository.count();
        AvailablePin availablePin = pinRepository.findOne(count);
        if (availablePin == null)
            throw new RuntimeException("Could not generate a new pin");

        pinRepository.delete(count);
        return availablePin.getPin();
    }
}
