package com.horeca.site.services.accounts;

import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.accounts.AccountPOST;
import com.horeca.site.models.accounts.AccountPending;
import com.horeca.site.repositories.accounts.AccountPendingRepository;
import com.horeca.site.services.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public abstract class AccountPendingService<T extends AccountPending> {

    @Autowired
    private EmailSenderService emailSenderService;

    protected abstract AccountPendingRepository<T> getRepository();

    protected abstract String getActivationUrl();

    public abstract AccountPending add(AccountPOST accountPOST);

    public abstract void activate(String secret);

    protected T getBySecret(String secret) {
        T pending = getRepository().findBySecret(secret);
        if (pending == null) {
            throw new ResourceNotFoundException();
        }
        return pending;
    }

    public void sendActivationEmail(AccountPending account) throws UnsupportedEncodingException, MessagingException {
        String link = getActivationUrl() + account.getSecret();
        String messageBody =
                "<div>" +
                        "Hi," +
                        "<br /><br />" +
                        "to activate your account, please click the link below:<br/>" + link +
                        "<br/><br />" +
                        "Regards," +
                        "<br/>" +
                        "The Throdi Team" +
                        "</div>";

        emailSenderService.sendStandard("Account activation", messageBody, account.getEmail());
    }
}
