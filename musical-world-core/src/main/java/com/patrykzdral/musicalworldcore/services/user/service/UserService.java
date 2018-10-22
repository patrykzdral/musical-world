package com.patrykzdral.musicalworldcore.services.user.service;

import com.patrykzdral.musicalworldcore.persistance.entity.User;

import java.util.Optional;

public interface UserService {
    public Optional<User> findByEmail(String email);

    public void changeUserPassword(final User user, final String password);

    public Optional<User> findByUsername(String username);

    public Iterable<User> findAll();

}
