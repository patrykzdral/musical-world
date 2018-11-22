package com.patrykzdral.musicalworldcore.controller.user;

import com.patrykzdral.musicalworldcore.listener.OnResetPasswordEvent;
import com.patrykzdral.musicalworldcore.persistance.entity.User;
import com.patrykzdral.musicalworldcore.services.user.service.ResetUserPasswordService;
import com.patrykzdral.musicalworldcore.services.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping(value = "/user/requestResetPassword", method = RequestMethod.POST)
    public ResponseEntity<Object> resetPassword(final HttpServletRequest request, @RequestParam("email") final String userEmail) {
        User user = resetUserPasswordService.resetUserPassword(userEmail);

        eventPublisher.publishEvent(new OnResetPasswordEvent(user, request.getLocale(), getAppUrl(request)));
        return ResponseEntity.ok(user);
    }

    @RequestMapping(value = "/user/confirmResetPassword", method = RequestMethod.POST)
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
