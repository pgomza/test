package com.horeca.site.services;

import com.google.common.hash.Hashing;
import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.hotel.Hotel;
import com.horeca.site.models.user.UserInfo;
import com.horeca.site.repositories.HotelRepository;
import com.horeca.site.security.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class HotelService {

    @Autowired
    private HotelRepository repository;

    @Autowired
    private LoginService loginService;

    @PostConstruct
    private void addHardcodedPanelClient() {
        String hardcodedUsername = "admin";
        if (!loginService.isAlreadyPresent(hardcodedUsername)) {
            List<String> roles = new ArrayList<>(Arrays.asList("ROLE_HOTEL_1"));
            String hardcodedPassword = Hashing.sha256().hashString("throdi", StandardCharsets.UTF_8).toString();
            UserInfo userInfo = new UserInfo(UserInfo.AUTH_PREFIX_LOGIN + hardcodedUsername, hardcodedPassword, roles);
            loginService.saveUser(userInfo);
        }
    }

    public Iterable<Hotel> getAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Hotel get(Long id) {
        Hotel hotel = repository.findOne(id);
        if (hotel == null)
            throw new ResourceNotFoundException();

        return hotel;
    }

    public Hotel add(Hotel hotel) {
        return repository.save(hotel);
    }

    public Hotel update(Long id, Hotel newOne) {
        Hotel oldOne = get(id);
        newOne.setId(id);
        Hotel changed = repository.save(newOne);

        return changed;
    }

    public void delete(Long id) {
        Hotel toDelete = get(id);
        repository.delete(toDelete);
    }
}
