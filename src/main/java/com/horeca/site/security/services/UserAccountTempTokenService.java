package com.horeca.site.security.services;

import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.hotel.Hotel;
import com.horeca.site.repositories.UserAccountTempTokenRepository;
import com.horeca.site.security.models.UserAccountTempToken;
import com.horeca.site.services.HotelService;
import org.apache.log4j.Logger;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
public class UserAccountTempTokenService {

    private static final Logger logger = Logger.getLogger(UserAccountTempTokenService.class);
    private static final String NOT_FOUND_OR_INVALID_MESSAGE =
            "The requested token has been invalidated or has never existed";

    @Value("${tempToken.validitySeconds}")
    private Integer tempTokenValiditySeconds;

    @Autowired
    private HotelService hotelService;

    @Autowired
    private UserAccountTempTokenRepository repository;

    public UserAccountTempToken generateTempToken(Long hotelId, Set<String> roles) {
        String token = generateRandomString();
        Hotel hotel = hotelService.get(hotelId);

        LocalDateTime createdAt = new LocalDateTime();
        LocalDateTime expiresAt = new LocalDateTime().plusSeconds(tempTokenValiditySeconds);

        UserAccountTempToken tempToken = new UserAccountTempToken(token, hotel, roles, createdAt, expiresAt);
        return repository.save(tempToken);
    }

    public UserAccountTempToken get(String token) {
        UserAccountTempToken tempToken = repository.findOne(token);
        if (tempToken == null)
            throw new ResourceNotFoundException(NOT_FOUND_OR_INVALID_MESSAGE);
        return tempToken;
    }

    public void delete(String token) {
        repository.delete(token);
    }

    public int getSecondsUntilExpiration(UserAccountTempToken tempToken) {
        LocalDateTime expiresAt = tempToken.getExpiresAt();
        Long expiresIn = (expiresAt.toDateTime().getMillis() - (new LocalDateTime().toDateTime().getMillis())) / 1000L;
        return expiresIn.intValue();
    }

    public void ensureValidity(UserAccountTempToken tempToken) {
        if (getSecondsUntilExpiration(tempToken) <= 0) {
            throw new ResourceNotFoundException(NOT_FOUND_OR_INVALID_MESSAGE);
        }
    }

    private String generateRandomString() {
        return UUID.randomUUID().toString();
    }

    @Scheduled(fixedDelay = 60 * 60 * 1000) // execute every hour
    public void periodicallyDeleteInvalidTokens() {
         logger.info("Checking for invalid temp tokens...");

        Iterable<UserAccountTempToken> tempTokens = repository.findAll();
        Set<UserAccountTempToken> toDelete = new HashSet<>();
        for (UserAccountTempToken tempToken : tempTokens) {
            if (getSecondsUntilExpiration(tempToken) <= 0)
                toDelete.add(tempToken);
        }
        repository.delete(toDelete);

        logger.info(toDelete.size() + " invalid temp tokens have been deleted");
    }
}
