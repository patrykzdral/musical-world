package com.patrykzdral.musicalworldcore.controller.user;

import com.patrykzdral.musicalworldcore.listener.OnResetPasswordEvent;
import com.patrykzdral.musicalworldcore.persistance.entity.User;
import com.patrykzdral.musicalworldcore.services.user.model.PasswordDto;
import com.patrykzdral.musicalworldcore.services.user.service.RegisterUserService;
import com.patrykzdral.musicalworldcore.services.user.service.ResetUserPasswordService;
import com.patrykzdral.musicalworldcore.services.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Locale;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.ok;

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

    @RequestMapping(value = "/user/resetPassword", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Object> resetPassword(final HttpServletRequest request, @RequestParam("email") final String userEmail) {
        User user = resetUserPasswordService.resetUserPassword(userEmail);

        eventPublisher.publishEvent(new OnResetPasswordEvent(user, request.getLocale(), getAppUrl(request)));
        return  ResponseEntity.ok(user);
    }
    private String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

//    @RequestMapping(value = "/user/changePassword", method = RequestMethod.GET)
//    public String showChangePasswordPage(final Locale locale, final Model dto, @RequestParam("id") final long id, @RequestParam("token") final String token) {
//        final String result = securityUserService.validatePasswordResetToken(id, token);
//        if (result != null) {
//            dto.addAttribute("message", messages.getMessage("auth.message." + result, null, locale));
//            return "redirect:/login?lang=" + locale.getLanguage();
//        }
//        return "redirect:/updatePassword.html?lang=" + locale.getLanguage();
//    }
//
//    @RequestMapping(value = "/user/updatePassword", method = RequestMethod.POST)
//    @ResponseBody
//    public ResponseEntity<String> changeUserPassword(final Locale locale, @Valid PasswordDto passwordDto) {
//        Optional<User> optionalUser = userService.findUserByEmail(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail());
//        if (!resetUserPasswordService.checkIfValidOldPassword(user, passwordDto.getOldPassword())) {
//            throw new InvalidOldPasswordException();
//        }
//        userService.changeUserPassword(user, passwordDto.getNewPassword());
//        resetUserPasswordService.changePassword(passwordDto);
//        return ResponseEntity.ok("Password updated successfully");
//    }
}
