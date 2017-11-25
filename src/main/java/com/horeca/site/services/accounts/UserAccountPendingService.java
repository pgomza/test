package com.horeca.site.services.accounts;

import com.horeca.site.exceptions.BusinessRuleViolationException;
import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.exceptions.UnauthorizedException;
import com.horeca.site.models.accounts.AccountPOST;
import com.horeca.site.models.accounts.AccountPending;
import com.horeca.site.models.accounts.UserAccountPending;
import com.horeca.site.models.accounts.UserAccountTempToken;
import com.horeca.site.models.hotel.Hotel;
import com.horeca.site.repositories.accounts.AccountPendingRepository;
import com.horeca.site.security.services.PasswordHashingService;
import com.horeca.site.security.services.UserAccountService;
import com.horeca.site.services.EmailSenderService;
import com.horeca.site.services.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserAccountPendingService extends AccountPendingService<UserAccountPending> {

    private final UserAccountService userAccountService;
    private final UserAccountTempTokenService userAccountTempTokenService;
    private final HotelService hotelService;

    @Autowired
    public UserAccountPendingService(EmailSenderService emailSenderService,
                                     AccountPendingRepository<UserAccountPending> repository,
                                     @Value("${activation.url.users}") String activationUrl,
                                     UserAccountService userAccountService,
                                     UserAccountTempTokenService userAccountTempTokenService,
                                     HotelService hotelService) {

        super(emailSenderService, repository, activationUrl);
        this.userAccountService = userAccountService;
        this.userAccountTempTokenService = userAccountTempTokenService;
        this.hotelService = hotelService;
    }

    public AccountPending verifyAndAdd(String token, AccountPOST accountPOST) {
        String email = accountPOST.getEmail();
        String plainTextPassword = accountPOST.getPassword();
        final String DUMMY_REDIRECTION_URL = "https://example.com";

        UserAccountTempToken tempToken;
        try {
            tempToken = userAccountTempTokenService.get(token);
            userAccountTempTokenService.ensureValidity(tempToken);
        }
        catch (ResourceNotFoundException ex) {
            throw new UnauthorizedException(ex); // in that case this exception makes more sense
        }

        if (userAccountService.exists(email)) {
            throw new BusinessRuleViolationException("A user with such an email already exists");
        }

        String hashedPassword = PasswordHashingService.getHashedFromPlain(plainTextPassword);
        String secret = userAccountTempTokenService.generateRandomString();
        UserAccountPending userAccountPending =
                new UserAccountPending(email, hashedPassword, tempToken.getHotel().getId(), secret, DUMMY_REDIRECTION_URL);
        UserAccountPending saved = repository.save(userAccountPending);

        // to prevent people from creating several user accounts with the same temp token, invalidate it
        userAccountTempTokenService.invalidate(tempToken);

        return saved;
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

        userAccountService.create(email, password, true, hotelId);

        // this may be the first user for this hotel
        // make sure that the hotel contains enough information
        hotelService.ensureEnoughInfoAboutHotel(hotelId);

        Hotel hotel = hotelService.get(hotelId);
        hotel.setIsThrodiPartner(true);
        hotelService.update(hotel.getId(), hotel);

        repository.delete(email);
    }
}
