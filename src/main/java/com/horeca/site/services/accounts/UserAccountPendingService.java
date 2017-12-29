package com.horeca.site.services.accounts;

import com.horeca.site.exceptions.BusinessRuleViolationException;
import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.accounts.AccountPending;
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

import java.util.Arrays;
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

        hotelService.get(hotelId);

        String hashedPassword = PasswordHashingService.getHashedFromPlain(plainTextPassword);
        String secret = generateSecret();
        UserAccountPending userAccountPending = new UserAccountPending(email, hashedPassword, secret, hotelId, true);

        return repository.save(userAccountPending);
    }

    @Override
    public void activate(String secret) {
        UserAccountPending pending;
        try {
            pending = getBySecret(secret);
        } catch (ResourceNotFoundException ex) {
            throw new BusinessRuleViolationException("Invalid secret");
        }

        String email = pending.getEmail();
        String password = pending.getPassword();
        Long hotelId = pending.getHotelId();
        Boolean fullAccess = pending.getFullAccess();

        List<String> roles = Arrays.asList(UserAccount.ROLE_DEFAULT);
        if (fullAccess) {
            roles.add(UserAccount.ROLE_HOTEL_FULL);
        }
        userAccountService.create(email, password, true, hotelId, roles);

        // this may be the first user for this hotel
        // make sure that the hotel contains enough information
        hotelService.ensureEnoughInfoAboutHotel(hotelId);

        Hotel hotel = hotelService.get(hotelId);
        hotel.setIsThrodiPartner(true);
        hotelService.update(hotel.getId(), hotel);

        repository.delete(email);
    }
}
