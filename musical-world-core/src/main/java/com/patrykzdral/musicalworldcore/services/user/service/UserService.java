package com.patrykzdral.musicalworldcore.services.user.service;

import com.patrykzdral.musicalworldcore.persistance.entity.User;
import com.patrykzdral.musicalworldcore.services.user.model.UserWithPhotoDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

public interface UserService {
    public Optional<User> findByEmail(String email);

    public void changeUserPassword(final User user, final String password);

    public Optional<User> findByUsername(String username);

    public Iterable<User> findAll();

    void saveUserPhoto(MultipartFile file, User user) throws IOException;

    User deleteAccount(String username);

    public UserWithPhotoDTO findUserWithPhotoByUsername(String username) throws IOException;

    void assignPhotoToUser(MultipartFile file, String username) throws IOException;

    User updateAccount(UserWithPhotoDTO user);
}
