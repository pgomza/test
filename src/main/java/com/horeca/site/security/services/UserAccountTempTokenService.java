package com.horeca.site.security.services;

import com.horeca.site.models.hotel.Hotel;
import com.horeca.site.repositories.UserAccountTempTokenRepository;
import com.horeca.site.security.models.UserAccountTempToken;
import com.horeca.site.services.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
public class UserAccountTempTokenService {

    @Value("${tempToken.validitySeconds}")
    private Integer tempTokenValiditySeconds;

    @Autowired
    private HotelService hotelService;

    @Autowired
    private UserAccountTempTokenRepository repository;

    public UserAccountTempToken generateTempToken(Long hotelId, Set<String> roles) {
        String token = generateRandomString();
        Hotel hotel = hotelService.get(hotelId);

        long currentTimeMillis = System.currentTimeMillis();
        Timestamp createdAt = new Timestamp(currentTimeMillis);
        Timestamp expiresAt = new Timestamp(currentTimeMillis + tempTokenValiditySeconds * 1000L);

        UserAccountTempToken tempToken = new UserAccountTempToken(token, hotel, roles, createdAt, expiresAt);
        return repository.save(tempToken);
    }

    private String generateRandomString() {
        return UUID.randomUUID().toString();
    }
}
