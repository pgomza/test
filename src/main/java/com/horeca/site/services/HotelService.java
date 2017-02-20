package com.horeca.site.services;

import com.google.common.hash.Hashing;
import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.extractors.HotelDataExtractor;
import com.horeca.site.models.hotel.Hotel;
import com.horeca.site.models.hotel.HotelView;
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

@Service
@Transactional
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

    public List<Hotel> getAll() {
        Iterable<Hotel> hotels = repository.findAll();
        List<Hotel> hotelList = new ArrayList<>();
        for (Hotel hotel : hotels) {
            hotelList.add(hotel);
        }

        return hotelList;
    }

    public List<HotelView> getAllViews() {
        List<HotelView> hotelViews = new ArrayList<>();
        for (Hotel hotel : getAll()) {
            hotelViews.add(hotel.toView());
        }

        return hotelViews;
    }

    public Hotel get(Long id) {
        Hotel hotel = repository.findOne(id);
        if (hotel == null)
            throw new ResourceNotFoundException();

        return hotel;
    }

    public HotelView getView(Long id) {
        Hotel hotel = get(id);
        return hotel.toView();
    }

    public Hotel add(Hotel hotel) {
        return repository.save(hotel);
    }

    public Iterable<Hotel> addAll(Iterable<Hotel> hotels) {
        return repository.save(hotels); // don't return a populated List due to performance reasons
    }

    public Hotel update(Long id, Hotel newOne) {
        newOne.setId(id);
        Hotel changed = repository.save(newOne);

        return changed;
    }

    public void delete(Long id) {
        Hotel toDelete = get(id);
        repository.delete(toDelete);
    }

    public Hotel convertFromHotelData(HotelDataExtractor.HotelData hotelData) {
        Hotel hotel = new Hotel();
        hotel.setName(hotelData.title);
        hotel.setDescription(hotelData.description);
        hotel.setAddress(hotelData.addressFull);
        hotel.setEmail(hotelData.email);
        hotel.setWebsite(hotelData.website);
        hotel.setPhone(hotelData.website);
        hotel.setFax(hotelData.fax);
        hotel.setStarRating(hotelData.starRating);
        hotel.setRooms(hotelData.rooms);
        hotel.setRatingOverall(hotelData.ratingOverall);
        hotel.setRatingOverallText(hotelData.ratingOverallText);
        hotel.setPropertyType(hotelData.propertyType);
        hotel.setChain(hotelData.chain);

        return hotel;
    }
}
