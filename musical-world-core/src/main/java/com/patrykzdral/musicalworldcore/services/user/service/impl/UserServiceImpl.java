package com.patrykzdral.musicalworldcore.services.user.service.impl;

import com.patrykzdral.musicalworldcore.persistance.entity.Picture;
import com.patrykzdral.musicalworldcore.persistance.entity.User;
import com.patrykzdral.musicalworldcore.persistance.entity.UserReference;
import com.patrykzdral.musicalworldcore.persistance.repository.UserReferenceRepository;
import com.patrykzdral.musicalworldcore.persistance.repository.UserRepository;
import com.patrykzdral.musicalworldcore.services.user.model.UserWithPhotoDTO;
import com.patrykzdral.musicalworldcore.services.user.service.UserService;
import com.patrykzdral.musicalworldcore.validation.exception.ApplicationException;
import com.patrykzdral.musicalworldcore.validation.exception.ExceptionCode;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserReferenceRepository userReferenceRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserReferenceRepository userReferenceRepository,
                           @Qualifier("bCryptPasswordEncoder") PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.userReferenceRepository = userReferenceRepository;
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
    public void saveUserPhoto(MultipartFile file, User user) throws IOException {
        Picture pic = Picture.builder()
                .fileName(file.getOriginalFilename())
                .mimetype(file.getContentType())
                .pic(file.getBytes())
                .build();
        user.setPicture(pic);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteAccount(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        List<UserReference> userReferences = userReferenceRepository.findByUser(username);
        Optional.of(userReferences).ifPresent(
                userReferenceRepository::deleteAll
        );
        optionalUser.ifPresentOrElse(user -> {
                    userRepository.deleteById(user.getId());

                }, () -> {
                    throw new ApplicationException(ExceptionCode.EXCEPTION_0012, "User does not exists");
                }
        );
    }

    @Override
    public UserWithPhotoDTO findUserWithPhotoByUsername(String username) throws IOException {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            return convertToDto(optionalUser.get());
        } else throw new ApplicationException(ExceptionCode.EXCEPTION_0012, "User does not exists");

    }

    @Override
    public void assignPhotoToUser(MultipartFile file, String username) throws IOException {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            Picture picture = Picture.builder()
                    .fileName(file.getOriginalFilename())
                    .mimetype(file.getContentType())
                    .pic(file.getBytes())
                    .build();
            optionalUser.get().setPicture(picture);
            userRepository.save(optionalUser.get());
        } else throw new ApplicationException(ExceptionCode.EXCEPTION_0012, "User does not exists");
    }

    @Override
    public User updateAccount(UserWithPhotoDTO userWithPhotoDTO, String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get().toBuilder()
                    .firstName(userWithPhotoDTO.getFirstName())
                    .lastName(userWithPhotoDTO.getLastName())
                    .description(userWithPhotoDTO.getDescription())
                    .phoneNumber(userWithPhotoDTO.getPhoneNumber())
                    .build();
            userRepository.save(user);
            return optionalUser.get();
        } else throw new ApplicationException(ExceptionCode.EXCEPTION_0012, "User does not exists");
    }

    private UserWithPhotoDTO convertToDto(User user) throws IOException {
        byte[] photo;

        UserWithPhotoDTO dto = modelMapper.map(user, UserWithPhotoDTO.class);
        Optional<Picture> concertPhoto = Optional.ofNullable(user.getPicture());
        if (concertPhoto.isPresent()) {
            photo = concertPhoto.get().getPic();
            dto.setPhoto("data:image/png;base64," + Base64.getEncoder().encodeToString(photo));
        }

        return dto;
    }


}
