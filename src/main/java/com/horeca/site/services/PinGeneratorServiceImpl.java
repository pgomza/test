package com.horeca.site.services;

import com.horeca.site.models.stay.Stay;
import com.horeca.site.repositories.services.StayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ThreadLocalRandom;

@Service
@Transactional
public class PinGeneratorServiceImpl implements PinGeneratorService {

    @Autowired
    private StayRepository stayRepository;

    private final char[] CHARSET = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray();

    private boolean isAvailable(String pin) {
        Stay stay = stayRepository.findOne(pin);
        if (stay == null)
            return true;

        return false;
    }

    // Given the fact that the pin consists of 6 alphanumeric, lowercase
    // characters, there are (26 + 10)^6 = 2176782336 possible combinations
    // it's unlikely that the newly generated pin will already be taken
    @Override
    @Transactional
    public String generatePin() {
        String pin = null;
        boolean generated = false;
        char result[] = new char[PIN_LENGTH];

        while (!generated) {
            for (int i = 0; i < PIN_LENGTH; i++) {
                int index = ThreadLocalRandom.current().nextInt(CHARSET.length);
                result[i] = CHARSET[index];
            }

            pin = new String(result);
            if (isAvailable(pin))
                generated = true;
        }

        return pin;
    }
}
