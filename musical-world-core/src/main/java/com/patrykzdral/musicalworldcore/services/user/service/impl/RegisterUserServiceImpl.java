package com.patrykzdral.musicalworldcore.services.user.service.impl;

import com.patrykzdral.musicalworldcore.persistance.entity.User;
import com.patrykzdral.musicalworldcore.persistance.repository.RoleRepository;
import com.patrykzdral.musicalworldcore.persistance.repository.UserRepository;
import com.patrykzdral.musicalworldcore.services.user.exception.InternalException;
import com.patrykzdral.musicalworldcore.services.user.model.RegisterUserRequestDTO;
import com.patrykzdral.musicalworldcore.services.user.service.RegisterUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class RegisterUserServiceImpl implements RegisterUserService {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public RegisterUserServiceImpl(RoleRepository roleRepository, @Qualifier("bCryptPasswordEncoder") PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
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

    private boolean usernameAlreadyInDB(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    private boolean emailIsAlreadyInDB(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

}
