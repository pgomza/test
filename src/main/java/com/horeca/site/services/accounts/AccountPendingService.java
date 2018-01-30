package com.horeca.site.services.accounts;

import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.accounts.AccountPending;
import com.horeca.site.repositories.accounts.AccountPendingRepository;
import com.horeca.site.services.EmailSenderService;
import org.springframework.beans.factory.annotation.Value;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class AccountPendingService<T extends AccountPending> {

    private final EmailSenderService emailSenderService;
    protected final AccountPendingRepository<T> repository;
    protected final String activationUrl;

    @Value("${activation.redirectionUrl}")
    protected String redirectionUrl;

    public AccountPendingService(EmailSenderService emailSenderService, AccountPendingRepository<T> repository,
                                 String activationUrl) {
        this.emailSenderService = emailSenderService;
        this.repository = repository;
        this.activationUrl = activationUrl;
    }

    public abstract void activate(String email);

    public T get(String email) {
        T pending = repository.findOne(email);
        if (pending == null) {
            throw new ResourceNotFoundException();
        }
        return pending;
    }

    public List<T> getAll() {
        List<T> result = new ArrayList<>();
        repository.findAll().forEach(result::add);
        return result;
    }

    protected T getBySecret(String secret) {
        T pending = repository.findBySecret(secret);
        if (pending == null) {
            throw new ResourceNotFoundException();
        }
        return pending;
    }

    protected static String generateSecret() {
        return UUID.randomUUID().toString();
    }

    public void sendActivationEmail(AccountPending account) throws UnsupportedEncodingException, MessagingException {
        String link = activationUrl + account.getSecret();
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

    public static ResponseMessage prepareResponseMessage(String email) {
        return new ResponseMessage("The activation link has been sent to " + email);
    }

    public String prepareRedirectPage(String outcome) {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Account activation</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <b>" + outcome + "</b>; redirecting to <a href=\"" + redirectionUrl + "\">" + redirectionUrl + "</a> in \n" +
                "    <span id=\"counter\">5</span><span id=\"counter-text\"> seconds</span>\n" +
                "\n" +
                "    <script>\n" +
                "        setInterval(function() {\n" +
                "            var counterSpan = document.querySelector(\"#counter\");\n" +
                "            var counterTextSpan = document.querySelector(\"#counter-text\");\n" +
                "\n" +
                "            var count = counterSpan.textContent * 1 - 1;\n" +
                "\n" +
                "            if (count === 1) {\n" +
                "                counterTextSpan.textContent = \" second\"\n" +
                "            }\n" +
                "\n" +
                "            if (count > 0) {\n" +
                "                counterSpan.textContent = count;\n" +
                "            }\n" +
                "            else {\n" +
                "                window.location.href=\"" + redirectionUrl + "\";\n" +
                "            }\n" +
                "        }, 1000);\n" +
                "    </script>\n" +
                "</body>\n" +
                "</html>";
    }

    public static class ResponseMessage {
        private String message;

        public ResponseMessage(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
