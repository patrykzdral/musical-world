package com.patrykzdral.musicalworldcore.controller.oauth;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OAuthController {
    @GetMapping(value = "api/oauth2check",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Object getLoggedUserBasicInfo(Authentication authentication) {
        return authentication.getPrincipal();
    }

    @GetMapping(value = "api/check-access", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Object checkAccess() {
        return true;
    }
}
