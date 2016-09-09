package com.horeca.site.services;

import com.horeca.site.models.Stay;
import com.horeca.site.repositories.StayRepository;
import com.horeca.site.security.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
public class StayRegistrationService {

    @Autowired
    private StayRepository stayRepository;
    @Autowired
    private LoginService loginService;
    private LinkedList<String> availablePins = new LinkedList<>();

    //TODO change this prototype, buggy and inefficient implementation
    @PostConstruct
    private void findAvailablePins() {
        Iterable<Stay> allStays = stayRepository.findAll();
        Set<String> alreadyTaken = new HashSet<>();
        for (Stay stay : allStays) {
            alreadyTaken.add(stay.getPin());
        }

        for (int i = 1000; i < 2000; i++) {
            if (!alreadyTaken.contains(Integer.toString(i)))
                availablePins.add(Integer.toString(i));
        }

        Collections.shuffle(availablePins);
    }

    public Stay registerNewStay(Stay stay) {
        String pin = getRandomPin();
        stay.setPin(pin);
        Stay added = stayRepository.save(stay);

        Collection<SimpleGrantedAuthority> authorities = Collections.unmodifiableCollection(Arrays.asList(new SimpleGrantedAuthority("ROLE_PIN")));
        String randomPassword = UUID.randomUUID().toString();
        UserDetails userDetails = new User(pin, randomPassword, authorities);
        loginService.saveUser(userDetails);

        return added;
    }

    private String getRandomPin() {
        if (availablePins.isEmpty())
            throw new RuntimeException("Could not generate a new pin"); //should never happen

        return availablePins.pop();
    }
}
