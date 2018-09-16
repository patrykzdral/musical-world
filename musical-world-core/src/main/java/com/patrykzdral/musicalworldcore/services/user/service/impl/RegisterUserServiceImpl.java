package com.patrykzdral.musicalworldcore.services.user.service.impl;

import com.patrykzdral.musicalworldcore.persistance.entity.User;
import com.patrykzdral.musicalworldcore.persistance.entity.VerificationToken;
import com.patrykzdral.musicalworldcore.persistance.repository.RoleRepository;
import com.patrykzdral.musicalworldcore.persistance.repository.TokenRepository;
import com.patrykzdral.musicalworldcore.persistance.repository.UserRepository;
import com.patrykzdral.musicalworldcore.services.user.exception.InternalException;
import com.patrykzdral.musicalworldcore.services.user.model.RegisterUserRequestDTO;
import com.patrykzdral.musicalworldcore.services.user.service.RegisterUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Collections;

@Service
public class RegisterUserServiceImpl implements RegisterUserService {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private static final String TOKEN_INVALID = "invalidToken";
    private static final String TOKEN_EXPIRED = "expired";
    private static final String TOKEN_VALID = "valid";

    @Autowired
    public RegisterUserServiceImpl(RoleRepository roleRepository, @Qualifier("bCryptPasswordEncoder") PasswordEncoder passwordEncoder, UserRepository userRepository, TokenRepository tokenRepository) {
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
    }


    @Override
    public User registerUserAccount(RegisterUserRequestDTO registerUserRequestDTO) {
        if (emailIsAlreadyInDB(registerUserRequestDTO.getEmail())) {
            throw new InternalException("REGISTER_EXCEPTION", "Email is already in DB");
        }
        if (usernameAlreadyInDB(registerUserRequestDTO.getUsername())) {
            throw new InternalException("REGISTER_EXCEPTION", "Username is already in DB");
        }
        return userRepository.save(User.builder()
                .username(registerUserRequestDTO.getUsername())
                .email(registerUserRequestDTO.getEmail())
                .password("{bcrypt}" + passwordEncoder.encode(registerUserRequestDTO.getPassword()))
                .confirmed(false)
                .rememberMe(false)
                .roles(Collections.singleton(roleRepository.findByName("BASIC_USER")))
                .build());
    }

    @Override
    public String validateVerificationToken(String token) {
        final VerificationToken verificationToken = tokenRepository.findByToken(token);
        if (verificationToken == null) {
            return TOKEN_INVALID;
        }

        final User user = verificationToken.getUser();
        final Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            tokenRepository.delete(verificationToken);
            return TOKEN_EXPIRED;
        }

        user.setConfirmed(true);
        // tokenRepository.delete(verificationToken);
        userRepository.save(user);
        return TOKEN_VALID;
    }

    @Override
    public void createVerificationTokenForUser(User user, String token) {
        final VerificationToken myToken = new VerificationToken(token, user);
        tokenRepository.save(myToken);
    }

    private boolean usernameAlreadyInDB(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    private boolean emailIsAlreadyInDB(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

}
