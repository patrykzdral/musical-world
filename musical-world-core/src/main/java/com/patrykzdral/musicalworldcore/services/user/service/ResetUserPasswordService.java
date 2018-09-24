package com.patrykzdral.musicalworldcore.services.user.service;

import com.patrykzdral.musicalworldcore.persistance.entity.User;

public interface ResetUserPasswordService {
    User resetUserPassword(String email);
    void createPasswordResetTokenForUser(User user, String token);
    boolean checkIfValidOldPassword(final User user, final String oldPassword);

}
