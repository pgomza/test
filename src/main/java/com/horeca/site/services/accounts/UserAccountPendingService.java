package com.horeca.site.services.accounts;

import com.horeca.site.exceptions.BusinessRuleViolationException;
import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.accounts.AccountPending;
import com.horeca.site.models.accounts.UserAccountNewHotelPOST;
import com.horeca.site.models.accounts.UserAccountPOST;
import com.horeca.site.models.accounts.UserAccountPending;
import com.horeca.site.models.hotel.Hotel;
import com.horeca.site.repositories.accounts.AccountPendingRepository;
import com.horeca.site.security.models.UserAccount;
import com.horeca.site.security.services.PasswordHashingService;
import com.horeca.site.security.services.UserAccountService;
import com.horeca.site.services.EmailSenderService;
import com.horeca.site.services.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class UserAccountPendingService extends AccountPendingService<UserAccountPending> {

    private final UserAccountService userAccountService;
    private final HotelService hotelService;

    @Autowired
    public UserAccountPendingService(EmailSenderService emailSenderService,
                                     AccountPendingRepository<UserAccountPending> repository,
                                     @Value("${activation.url.users}") String activationUrl,
                                     UserAccountService userAccountService,
                                     HotelService hotelService) {

        super(emailSenderService, repository, activationUrl);
        this.userAccountService = userAccountService;
        this.hotelService = hotelService;
    }

    public AccountPending add(UserAccountPOST accountPOST) {
        String email = accountPOST.getEmail();
        String plainTextPassword = accountPOST.getPassword();
        Long hotelId = accountPOST.getHotelId();

        if (userAccountService.exists(email)) {
            throw new BusinessRuleViolationException("Such a user account already exists");
        }
        hotelService.get(hotelId);

        String hashedPassword = PasswordHashingService.getHashedFromPlain(plainTextPassword);
        String secret = generateSecret();
        UserAccountPending pending = new UserAccountPending();
        pending.setEmail(email);
        pending.setPassword(hashedPassword);
        pending.setSecret(secret);
        pending.setHotelId(hotelId);
        pending.setFullAccess(true);

        return repository.save(pending);
    }

    public AccountPending add(UserAccountNewHotelPOST accountPOST) {
        String email = accountPOST.getEmail();
        String plainTextPassword = accountPOST.getPassword();
        String hotelName = accountPOST.getHotel().getName();
        String hotelAddress = accountPOST.getHotel().getAddress();

        if (userAccountService.exists(email)) {
            throw new BusinessRuleViolationException("Such a user account already exists");
        }

        String hashedPassword = PasswordHashingService.getHashedFromPlain(plainTextPassword);
        String secret = generateSecret();
        UserAccountPending pending = new UserAccountPending();
        pending.setEmail(email);
        pending.setPassword(hashedPassword);
        pending.setSecret(secret);
        pending.setFullAccess(true);
        pending.setHotelName(hotelName);
        pending.setHotelAddress(hotelAddress);

        return repository.save(pending);
    }

    public void activateAsAnonymous(String secret) {
        UserAccountPending pending;
        try {
            pending = getBySecret(secret);
        } catch (ResourceNotFoundException ex) {
            throw new BusinessRuleViolationException("Invalid secret");
        }

        activate(pending.getEmail());
    }

    public void activate(String email) {
        UserAccountPending pending = get(email);

        String password = pending.getPassword();
        Boolean fullAccess = pending.getFullAccess();

        Long hotelId = pending.getHotelId();
        if (hotelId == null) {
            Hotel hotel = new Hotel();
            hotel.setName(pending.getHotelName());
            hotel.setAddress(pending.getHotelAddress());
            hotel.setIsThrodiPartner(true);
            Hotel added = hotelService.add(hotel);
            hotelId = added.getId();
        }

        List<String> roles = new ArrayList<>(Collections.singletonList(UserAccount.ROLE_DEFAULT));
        if (fullAccess) {
            roles.add(UserAccount.ROLE_HOTEL_FULL);
        }
        userAccountService.create(email, password, true, hotelId, roles);

        // this may be the first user for this hotel
        // make sure that the hotel contains enough information
        Hotel hotel = hotelService.get(hotelId);
        hotelService.fillInMissingInfoAndSave(hotel);

        // and is marked as a throdi partner
        hotel = hotelService.get(hotelId);
        hotel.setIsThrodiPartner(true);
        hotelService.update(hotel.getId(), hotel);

        repository.delete(email);
    }
}
