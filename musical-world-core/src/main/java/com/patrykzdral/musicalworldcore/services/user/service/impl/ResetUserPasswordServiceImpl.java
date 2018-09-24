package com.patrykzdral.musicalworldcore.services.user.service.impl;

import com.patrykzdral.musicalworldcore.persistance.entity.PasswordResetToken;
import com.patrykzdral.musicalworldcore.persistance.entity.User;
import com.patrykzdral.musicalworldcore.persistance.repository.PasswordResetTokenRepository;
import com.patrykzdral.musicalworldcore.persistance.repository.UserRepository;
import com.patrykzdral.musicalworldcore.services.user.exception.InternalException;
import com.patrykzdral.musicalworldcore.services.user.service.ResetUserPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ResetUserPasswordServiceImpl implements ResetUserPasswordService {
    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ResetUserPasswordServiceImpl(UserRepository userRepository, PasswordResetTokenRepository passwordResetTokenRepository, @Qualifier("bCryptPasswordEncoder") PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User resetUserPassword(String email) {
        User user;
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
        }
        else throw new InternalException("RESET_PASSWORD_EXCEPTION", "Email does not exists");
        return user;
    }

    @Override
    public void createPasswordResetTokenForUser(User user, String token) {
        final PasswordResetToken myToken = new PasswordResetToken(token, user);
        passwordResetTokenRepository.save(myToken);
    }

    @Override
    public boolean checkIfValidOldPassword(final User user, final String oldPassword) {
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }
}
