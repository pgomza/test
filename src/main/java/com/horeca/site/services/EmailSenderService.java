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
import java.util.Map;

@Service
public class EmailSenderService {

    private static final String EMAIL_LABS_POST_EMAIL_URL = "https://api.emaillabs.net.pl/api/new_sendmail";
    private static final String EMAIL_LABS_POST_EMAIL_TEMPLATE_URL = "https://api.emaillabs.net.pl/api/sendmail_templates";
    private static final String EMAIL_ADDRESS_FROM = "no-reply@throdi.com";
    private static final String EMAIL_NAME_FROM = "Throdi";

    @Value("${emailLabs.active}")
    private Boolean isEmailLabsActive;

    @Value("${emailLabs.key}")
    private String emailLabsKey;

    @Value("${emailLabs.secret}")
    private String emailLabsSecret;

    @Value("${emailLabs.smtp}")
    private String emailLabsSMTP;

    @Value("${emailLabs.templateId}")
    private String emailLabsTemplateId;

    @Autowired
    private JavaMailSender mailSender;

    public void sendStandard(String subject, String content, String addressTo)
            throws MessagingException, UnsupportedEncodingException {
        if (isEmailLabsActive) {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new FormHttpMessageConverter());

            // set the basic auth header
            restTemplate.getInterceptors().add(new BasicAuthorizationInterceptor(emailLabsKey, emailLabsSecret));

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("to[" + addressTo + "]", addressTo);

            params.add("from", EMAIL_ADDRESS_FROM);
            params.add("from_name", EMAIL_NAME_FROM);
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
            helper.setFrom(EMAIL_ADDRESS_FROM, EMAIL_NAME_FROM);
            helper.setSubject(subject);
            mailSender.send(mimeMessage);
        }
    }

    public void sendTemplate(String subject, String addressTo, Map<String, String> variables)
            throws MessagingException, UnsupportedEncodingException {

        if (isEmailLabsActive) { // just to be on the safe side
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new FormHttpMessageConverter());

            // set the basic auth header
            restTemplate.getInterceptors().add(new BasicAuthorizationInterceptor(emailLabsKey, emailLabsSecret));

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("to[" + addressTo + "]", addressTo);

            for (Map.Entry<String, String> entry : variables.entrySet()) {
                String name = entry.getKey();
                String value = entry.getValue();

                params.add("to[" + addressTo + "][vars][" + name + "]", value);
            }

            params.add("from", EMAIL_ADDRESS_FROM);
            params.add("from_name", EMAIL_NAME_FROM);
            params.add("smtp_account", emailLabsSMTP);
            params.add("subject", subject);
            params.add("template_id", emailLabsTemplateId);

            restTemplate.postForObject(EMAIL_LABS_POST_EMAIL_TEMPLATE_URL, params, String.class);
        }
    }
}
