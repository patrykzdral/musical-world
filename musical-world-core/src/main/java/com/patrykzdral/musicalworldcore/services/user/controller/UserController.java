package com.patrykzdral.musicalworldcore.services.user.controller;

import com.patrykzdral.musicalworldcore.listener.OnRegistrationCompleteEvent;
import com.patrykzdral.musicalworldcore.persistance.entity.User;
import com.patrykzdral.musicalworldcore.services.user.model.RegisterUserRequestDTO;
import com.patrykzdral.musicalworldcore.services.user.service.impl.RegisterUserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

@RestController
@Slf4j
public class UserController {

    private final RegisterUserServiceImpl registerUserService;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public UserController(RegisterUserServiceImpl registerUserServiceImpl, ApplicationEventPublisher eventPublisher) {
        this.registerUserService = registerUserServiceImpl;
        this.eventPublisher = eventPublisher;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/register")
    @ResponseBody
    public ResponseEntity<Object> save(@Valid @RequestBody RegisterUserRequestDTO registerUserRequestDTO, final HttpServletRequest request) {
        User user = registerUserService.registerUserAccount(registerUserRequestDTO);
        log.info(getAppUrl(request));
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user, request.getLocale(), getAppUrl(request)));
        return ResponseEntity.ok(user);
    }

    private String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

    @RequestMapping(value = "/registrationConfirm", method = RequestMethod.GET)
    public String confirmRegistration(final HttpServletRequest request, final Model model, @RequestParam("token") final String token) throws UnsupportedEncodingException {
        Locale locale = request.getLocale();
        final String result = registerUserService.validateVerificationToken(token);
        if (result.equals("valid")) {
//            final User user = userService.getUser(token);
//            authWithoutPassword(user);
//            model.addAttribute("message", messages.getMessage("message.accountVerified", null, locale));
            return "redirect:/console.html?lang=" + locale.getLanguage();
        }

        //model.addAttribute("message", messages.getMessage("auth.message." + result, null, locale));
        model.addAttribute("expired", "expired".equals(result));
        model.addAttribute("token", token);
        return "redirect:/badUser.html?lang=" + locale.getLanguage();
    }
}
