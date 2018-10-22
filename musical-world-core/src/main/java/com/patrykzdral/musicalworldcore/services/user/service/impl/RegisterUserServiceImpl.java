package com.patrykzdral.musicalworldcore.services.user.service.impl;

import com.patrykzdral.musicalworldcore.persistance.entity.User;
import com.patrykzdral.musicalworldcore.persistance.entity.VerificationToken;
import com.patrykzdral.musicalworldcore.persistance.repository.RoleRepository;
import com.patrykzdral.musicalworldcore.persistance.repository.UserRepository;
import com.patrykzdral.musicalworldcore.persistance.repository.VerificationTokenRepository;
import com.patrykzdral.musicalworldcore.services.user.exception.InternalException;
import com.patrykzdral.musicalworldcore.services.user.model.RegisterUserRequestDTO;
import com.patrykzdral.musicalworldcore.services.user.service.RegisterUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.Clock;
import java.time.Instant;
import java.util.Collections;
import java.util.Optional;

@Service
@Slf4j
public class RegisterUserServiceImpl implements RegisterUserService {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final Clock clock;
    private static final String TOKEN_INVALID = "invalidToken";
    private static final String TOKEN_EXPIRED = "expired";
    private static final String TOKEN_VALID = "valid";

    @Autowired
    public RegisterUserServiceImpl(RoleRepository roleRepository, @Qualifier("bCryptPasswordEncoder") PasswordEncoder passwordEncoder, UserRepository userRepository, VerificationTokenRepository verificationTokenRepository, Clock clock) {
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.clock = clock;
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
                .phoneNumber(registerUserRequestDTO.getPhoneNumber())
                .firstName(registerUserRequestDTO.getFirstName())
                .lastName(registerUserRequestDTO.getLastName())
                .confirmed(false)
                .rememberMe(false)
                .roles(Collections.singleton(roleRepository.findByName("BASIC_USER")))
                .build());
    }


    @Override
    public String validateVerificationToken(String token) {
        Optional<VerificationToken> optionalVerificationToken = verificationTokenRepository.findByToken(token);
        if (!optionalVerificationToken.isPresent()) {
            return TOKEN_INVALID;
        }
        VerificationToken verificationToken = optionalVerificationToken.get();
        final User user = verificationToken.getUser();
        if (verificationToken.getExpiryDate().getTime() - Date.from(Instant.now(clock)).getTime() <= 0) {
            verificationTokenRepository.delete(verificationToken);
            return TOKEN_EXPIRED;
        }

        user.setConfirmed(true);
        // verificationTokenRepository.delete(verificationToken);
        userRepository.save(user);
        return TOKEN_VALID;
    }

    @Override
    public void createVerificationTokenForUser(User user, String token) {
        final VerificationToken myToken = new VerificationToken(token, user);
        verificationTokenRepository.save(myToken);
    }


    private boolean usernameAlreadyInDB(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    private boolean emailIsAlreadyInDB(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

}
