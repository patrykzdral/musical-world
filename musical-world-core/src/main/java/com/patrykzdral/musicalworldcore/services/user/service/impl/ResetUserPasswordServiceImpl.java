package com.patrykzdral.musicalworldcore.services.user.service.impl;

import com.patrykzdral.musicalworldcore.persistance.entity.PasswordResetToken;
import com.patrykzdral.musicalworldcore.persistance.entity.User;
import com.patrykzdral.musicalworldcore.persistance.repository.PasswordResetTokenRepository;
import com.patrykzdral.musicalworldcore.persistance.repository.UserRepository;
import com.patrykzdral.musicalworldcore.validation.exception.ApplicationException;
import com.patrykzdral.musicalworldcore.services.user.service.ResetUserPasswordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Date;
import java.time.Clock;
import java.time.Instant;
import java.util.Optional;

@Service
@Slf4j
public class ResetUserPasswordServiceImpl implements ResetUserPasswordService {
    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final Clock clock;
    private static final String TOKEN_INVALID = "invalidToken";
    private static final String TOKEN_EXPIRED = "expired";
    private static final String TOKEN_VALID = "valid";
    @Autowired
    public ResetUserPasswordServiceImpl(UserRepository userRepository, PasswordResetTokenRepository passwordResetTokenRepository, @Qualifier("bCryptPasswordEncoder") PasswordEncoder passwordEncoder, Clock clock) {
        this.userRepository = userRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.clock = clock;
    }

    @Override
    public User resetUserPassword(String email) {
        User user;
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
        }
        else throw new ApplicationException("RESET_PASSWORD_EXCEPTION", "Email does not exists");
        return user;
    }

    @Override
    @Transactional
    public String validateResetTokenAndChangePassword(String token, String password) {
        log.info(token);
        Optional<PasswordResetToken> optionalPasswordResetToken = passwordResetTokenRepository.findByToken(token);
        if (!optionalPasswordResetToken.isPresent()) {
            return TOKEN_INVALID;
        }
        PasswordResetToken passwordResetToken = optionalPasswordResetToken.get();
        final User user = passwordResetToken.getUser();
        if (passwordResetToken.getExpiryDate().getTime() - Date.from(Instant.now(clock)).getTime() <= 0) {
            passwordResetTokenRepository.delete(passwordResetToken);
            return TOKEN_EXPIRED;
        }

        user.setPassword("{bcrypt}"+passwordEncoder.encode(password));
        userRepository.save(user);
        passwordResetTokenRepository.delete(passwordResetToken);
        return TOKEN_VALID;
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
