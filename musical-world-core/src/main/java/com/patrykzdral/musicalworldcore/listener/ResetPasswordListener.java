package com.patrykzdral.musicalworldcore.listener;

import com.patrykzdral.musicalworldcore.persistance.entity.User;
import com.patrykzdral.musicalworldcore.validation.exception.ApplicationException;
import com.patrykzdral.musicalworldcore.services.user.service.ResetUserPasswordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
public class ResetPasswordListener implements ApplicationListener<OnResetPasswordEvent> {
    private final ResetUserPasswordService resetUserPasswordService;

    private final MessageSource messages;

    private final JavaMailSender mailSender;

    private final Environment env;

    @Autowired
    public ResetPasswordListener(Environment env, JavaMailSender mailSender, @Qualifier("messageSource") MessageSource messages, ResetUserPasswordService resetUserPasswordService) {
        this.env = env;
        this.mailSender = mailSender;
        this.messages = messages;
        this.resetUserPasswordService = resetUserPasswordService;
    }

    @Override
    public void onApplicationEvent(final OnResetPasswordEvent event) {
        try {
            this.resetPassword(event);
        } catch (MessagingException e) {
            throw new ApplicationException("Sending mail error", e.getMessage());
        }
    }

    private void resetPassword(final OnResetPasswordEvent event) throws MessagingException {
        final User user = event.getUser();
        final String token = UUID.randomUUID().toString();
        resetUserPasswordService.createPasswordResetTokenForUser(user, token);

        final MimeMessage email = constructEmailMessage(event, user, token);
        mailSender.send(email);
    }


    private MimeMessage constructEmailMessage(final OnResetPasswordEvent event, final User user, final String token) throws MessagingException {
        String username = user.getUsername();
        final String recipientAddress = user.getEmail();
        final String subject = "Reset Password";
        final String resetPasswordUrl = "http://localhost:4200/#/auth/reset-password?token=" + token;
        log.info(resetPasswordUrl);
        //final String message = messages.getMessage("message sent", null, event.getLocale());
        MimeMessage message = mailSender.createMimeMessage();

        // use the true flag to indicate you need a multipart message
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(recipientAddress);
        helper.setSubject(subject);
        helper.setText(
                "<html>"
                        + "<body>"
                        + "<div>Dear "+username+","
                        + "<div><strong>This email has been sent to reset your password: </strong> <br/><br/></div>"
                        + "<div>"
                        + "<a href=" + resetPasswordUrl + "><img src='cid:link' style='float:center;'/>"
                        + "<br/><br/></div>"
                        + "<div>Thanks,</div>"
                        + "Musical world team"
                        + "</div></body>"
                        + "</html>", true);
        helper.addInline("link",
                new File("C:/Users/pzdral/Desktop/inżynierka/musical-world/musical-world-core/src/main/resources/reset.png"));
        helper.setFrom(env.getProperty("spring.mail.host"));
        return message;
    }
}
