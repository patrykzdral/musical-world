package com.patrykzdral.musicalworldcore.services.user.service;

import com.patrykzdral.musicalworldcore.persistance.entity.User;
import com.patrykzdral.musicalworldcore.services.user.model.RegisterUserRequestDTO;

public interface RegisterUserService {
    User registerUserAccount(RegisterUserRequestDTO registerUserRequestDTO);
    String validateVerificationToken(String token);
    void createVerificationTokenForUser(User user, String token);

}
