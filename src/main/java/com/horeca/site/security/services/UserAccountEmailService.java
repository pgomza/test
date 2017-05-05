package com.horeca.site.security.services;

import com.horeca.site.security.models.UserAccountPending;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class UserAccountEmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendActivation(UserAccountPending account) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        String link = "https://throdibackend.azurewebsites.net/api/accounts/users/activation?secret=" + account.getSecret();
        String messageBody =
                "<div>" +
                        "Hi," +
                        "<br/><br />" +
                        "please click on the following link to activate your account: " + link +
                        "<br/><br />" +
                        "Cheers," +
                        "<br/>" +
                        "The Throdi Team" +
                        "</div>";
        mimeMessage.setContent(messageBody, "text/html");
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
        helper.setTo(account.getEmail());
        helper.setSubject("Account activation");
        helper.setFrom("Throdi");
        mailSender.send(mimeMessage);
    }
}
