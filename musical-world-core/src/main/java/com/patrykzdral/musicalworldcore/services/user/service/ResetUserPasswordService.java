package com.patrykzdral.musicalworldcore.services.user.service;

import com.patrykzdral.musicalworldcore.persistance.entity.User;

import javax.transaction.Transactional;

public interface ResetUserPasswordService {
    User resetUserPassword(String email);
    String validateResetTokenAndChangePassword(String token, String password);
    void createPasswordResetTokenForUser(User user, String token);
    boolean checkIfValidOldPassword(final User user, final String oldPassword);

}
