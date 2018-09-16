package com.patrykzdral.musicalworldcore.listener;

import com.patrykzdral.musicalworldcore.persistance.entity.User;
import com.patrykzdral.musicalworldcore.services.user.exception.InternalException;
import com.patrykzdral.musicalworldcore.services.user.service.RegisterUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.UUID;

@Component
@Slf4j
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {
    private final RegisterUserService service;

    private final MessageSource messages;

    private final JavaMailSender mailSender;

    private final Environment env;

    @Autowired
    public RegistrationListener(Environment env, JavaMailSender mailSender, MessageSource messages, RegisterUserService service) {
        this.env = env;
        this.mailSender = mailSender;
        this.messages = messages;
        this.service = service;
    }

    @Override
    public void onApplicationEvent(final OnRegistrationCompleteEvent event) {
        try {
            this.confirmRegistration(event);
        } catch (MessagingException e) {
            throw new InternalException("Sending mail error", e.getMessage());
        }
    }

    private void confirmRegistration(final OnRegistrationCompleteEvent event) throws MessagingException {
        final User user = event.getUser();
        final String token = UUID.randomUUID().toString();
        service.createVerificationTokenForUser(user, token);

        final MimeMessage email = constructEmailMessage(event, user, token);
        mailSender.send(email);
    }


    private MimeMessage constructEmailMessage(final OnRegistrationCompleteEvent event, final User user, final String token) throws MessagingException {
        final String recipientAddress = user.getEmail();
        final String subject = "Registration Confirmation";
        final String confirmationUrl = event.getAppUrl() + "/registrationConfirm?token=" + token;
        log.info(confirmationUrl);
        //final String message = messages.getMessage("message sent", null, event.getLocale());
        MimeMessage message = mailSender.createMimeMessage();

        // use the true flag to indicate you need a multipart message
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(recipientAddress);
        helper.setSubject(subject);
        helper.setText(
                "<html>"
                        + "<body>"
                        + "<div>Dear User,"
                        + "<div><strong>please confirm registration using link below: </strong> <br/><br/></div>"
                        + "<div>"
                        + "<a href=" + confirmationUrl + "><img src='cid:link' style='float:center;'/>"
                        + "<br/><br/></div>"
                        + "<div>We hope you will use the app with pleasure!</div>"
                        + "<div>Thanks,</div>"
                        + "Musical world team"
                        + "</div></body>"
                        + "</html>", true);
        helper.addInline("link",
                new File("C:/register.png"));
        helper.setFrom(env.getProperty("spring.mail.host"));
        return message;
    }

}
