package com.patrykzdral.musicalworldcore.controller.user;

import com.patrykzdral.musicalworldcore.listener.OnRegistrationCompleteEvent;
import com.patrykzdral.musicalworldcore.listener.OnResetPasswordEvent;
import com.patrykzdral.musicalworldcore.persistance.entity.User;
import com.patrykzdral.musicalworldcore.services.user.model.RegisterUserRequestDTO;
import com.patrykzdral.musicalworldcore.services.user.service.RegisterUserService;
import com.patrykzdral.musicalworldcore.services.user.service.UserService;
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
public class RegistrationController {

    private final RegisterUserService registerUserService;
    private final UserService userService;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public RegistrationController(RegisterUserService registerUserService, UserService userService, ApplicationEventPublisher eventPublisher) {
        this.registerUserService = registerUserService;
        this.userService = userService;
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



    @RequestMapping(value = "/registrationConfirm", method = RequestMethod.GET)
    public String confirmRegistration(final HttpServletRequest request, final Model model, @RequestParam("token") final String token) throws UnsupportedEncodingException {
        Locale locale = request.getLocale();
        final String result = registerUserService.validateVerificationToken(token);
        if (result.equals("valid")) {
            return "redirect:/console.html?lang=" + locale.getLanguage();
        }
        //dto.addAttribute("message", messages.getMessage("auth.message." + result, null, locale));
        model.addAttribute("expired", "expired".equals(result));
        model.addAttribute("token", token);
        return "redirect:/badUser.html?lang=" + locale.getLanguage();
    }

    private String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
}
