package com.patrykzdral.musicalworldcore.controller.user;

import com.patrykzdral.musicalworldcore.listener.OnResetPasswordEvent;
import com.patrykzdral.musicalworldcore.persistance.entity.User;
import com.patrykzdral.musicalworldcore.services.user.service.ResetUserPasswordService;
import com.patrykzdral.musicalworldcore.services.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
public class ResetPasswordController {

    private final ResetUserPasswordService resetUserPasswordService;
    private final UserService userService;
    private final ApplicationEventPublisher eventPublisher;

    public ResetPasswordController(ResetUserPasswordService resetUserPasswordService, UserService userService, ApplicationEventPublisher eventPublisher) {
        this.resetUserPasswordService = resetUserPasswordService;
        this.userService = userService;
        this.eventPublisher = eventPublisher;
    }

    @PostMapping(value = "/user/requestResetPassword",produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public User resetPassword(final HttpServletRequest request, @RequestParam("email") final String userEmail) {
        User user = resetUserPasswordService.resetUserPassword(userEmail);
        eventPublisher.publishEvent(new OnResetPasswordEvent(user, request.getLocale(), getAppUrl(request)));
        return user;
    }

    @PostMapping(value = "/user/confirmResetPassword")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> confirmPasswordReset(@RequestParam("password") final String password, @RequestParam("token") final String token) {
        String message = resetUserPasswordService.validateResetTokenAndChangePassword(token, password);
        log.info(message);
        if (message.equals("valid"))
             return ResponseEntity.status(HttpStatus.OK).body(message);
        else return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);

    }

    private String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

}
