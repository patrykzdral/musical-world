package com.patrykzdral.musicalworldcore.services.user.service.impl;

import com.patrykzdral.musicalworldcore.persistance.entity.Picture;
import com.patrykzdral.musicalworldcore.persistance.entity.User;
import com.patrykzdral.musicalworldcore.persistance.repository.UserRepository;
import com.patrykzdral.musicalworldcore.services.picture.service.PictureService;
import com.patrykzdral.musicalworldcore.validation.exception.InternalException;
import com.patrykzdral.musicalworldcore.services.user.model.UserWithPhotoDTO;
import com.patrykzdral.musicalworldcore.services.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PictureService pictureService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    @Autowired
    public UserServiceImpl(UserRepository userRepository, PictureService pictureService, @Qualifier("bCryptPasswordEncoder") PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.pictureService = pictureService;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void changeUserPassword(final User user, final String password) {
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void saveUserPhoto(MultipartFile file, User user)throws IOException {
        Picture pic = Picture.builder()
                .fileName(file.getOriginalFilename())
                .mimetype(file.getContentType())
                .pic(file.getBytes())
                .build();
        user.setPicture(pic);
        userRepository.save(user);
    }

    @Override
    public User deleteAccount(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if(optionalUser.isPresent()){
            userRepository.deleteById(optionalUser.get().getId());
            return optionalUser.get();
        }
        else throw new InternalException("User exception", "User does not exists");
    }

    @Override
    public UserWithPhotoDTO findUserWithPhotoByUsername(String username) throws IOException {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if(optionalUser.isPresent()){
            return convertToDto(optionalUser.get());
        }
        else throw new InternalException("User exception", "User does not exists");

    }

    @Override
    public void assignPhotoToUser(MultipartFile file, String username) throws IOException {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if(optionalUser.isPresent()){
            Picture picture = Picture.builder()
                    .fileName(file.getOriginalFilename())
                    .mimetype(file.getContentType())
                    .pic(file.getBytes())
                    .build();
            optionalUser.get().setPicture(picture);
            userRepository.save(optionalUser.get());
        }
        else throw new InternalException("User exception", "User does not exists");
    }

    @Override
    public User updateAccount(UserWithPhotoDTO userWithPhotoDTO) {
        Optional<User> optionalUser = userRepository.findByUsername(userWithPhotoDTO.getUsername());
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            log.info(user.getDescription());
            User user2= user.toBuilder()
                    .firstName(userWithPhotoDTO.getFirstName())
                    .lastName(userWithPhotoDTO.getLastName())
                    .description(userWithPhotoDTO.getDescription())
                    .phoneNumber(userWithPhotoDTO.getPhoneNumber())
                    .build();
            log.info(user2.toString());
            userRepository.save(user2);
            return optionalUser.get();
        }
        else throw new InternalException("User exception", "User does not exists");
    }

    private UserWithPhotoDTO convertToDto(User user) throws IOException {

        UserWithPhotoDTO dto = modelMapper.map(user, UserWithPhotoDTO.class);
        byte[] photo = user.getPicture().getPic();

        StringBuilder base64 = new StringBuilder("data:image/png;base64,");
        base64.append(Base64.getEncoder().encodeToString(photo));
        dto.setPhoto(base64.toString());

        return dto;
    }



}
