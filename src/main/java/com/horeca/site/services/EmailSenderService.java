package com.horeca.site.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
public class EmailSenderService {

    private static final String EMAIL_LABS_POST_EMAIL_URL = "https://api.emaillabs.net.pl/api/new_sendmail";

    @Value("${emailLabs.active}")
    private Boolean isEmailLabsActive;

    @Value("${emailLabs.key}")
    private String emailLabsKey;

    @Value("${emailLabs.secret}")
    private String emailLabsSecret;

    @Value("${emailLabs.smtp}")
    private String emailLabsSMTP;

    @Autowired
    private JavaMailSender mailSender;

    public void send(String subject, String content, String addressTo, String addressFrom, String nameFrom)
            throws MessagingException, UnsupportedEncodingException {
        if (isEmailLabsActive) {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new FormHttpMessageConverter());

            // set the basic auth header
            restTemplate.getInterceptors().add(new BasicAuthorizationInterceptor(emailLabsKey, emailLabsSecret));

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("to[" + addressTo + "]", addressTo);
            params.add("from", addressFrom);
            params.add("from_name", nameFrom);
            params.add("smtp_account", emailLabsSMTP);
            params.add("subject", subject);
            params.add("html", content);

            restTemplate.postForObject(EMAIL_LABS_POST_EMAIL_URL, params, String.class);
        }
        else {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            mimeMessage.setContent(content, "text/html");
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
            helper.setTo(addressTo);
            if (nameFrom != null) {
                helper.setFrom(addressFrom, nameFrom);
            }
            else {
                helper.setFrom(addressFrom);
            }
            helper.setSubject(subject);
            mailSender.send(mimeMessage);
        }
    }
}
