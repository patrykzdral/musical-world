package com.patrykzdral.musicalworldcore.controller.user;

import com.patrykzdral.musicalworldcore.persistance.entity.User;
import com.patrykzdral.musicalworldcore.validation.exception.ApplicationException;
import com.patrykzdral.musicalworldcore.services.user.model.UserWithPhotoDTO;
import com.patrykzdral.musicalworldcore.services.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController {
    private final UserService userService;


    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteAccount(Authentication authentication) {
        userService.deleteAccount(((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername());
    }

    @PutMapping( consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<User> updateAccount(Authentication authentication, @Valid @RequestBody UserWithPhotoDTO userDTO) {
        log.info(userDTO.toString());
        User user= userService.updateAccount(userDTO,((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername());
        return ResponseEntity.ok(user);
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<User> findOne(@RequestParam("username") String username) {
        Optional<User> user = userService.findByUsername(username);
        if (!user.isPresent())
            throw new ApplicationException("User exception", "User does not exists");
        return ResponseEntity.ok(user.get());
    }

    @GetMapping(value = "with-photo")
    @ResponseBody
    public ResponseEntity<UserWithPhotoDTO> findUserWithPhoto(@RequestParam("username") String username) throws IOException {
        return ResponseEntity.ok(userService.findUserWithPhotoByUsername(username));
    }

    @PutMapping(value = "photo", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseBody
    public ResponseEntity<String> findOne(@RequestParam("file") MultipartFile file, Authentication authentication) {
        String message=null;
        Optional<User> user = userService.findByUsername(((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername());
        if (!user.isPresent())
            throw new ApplicationException("User exception", "User does not exists");

        try {
            userService.saveUserPhoto(file, user.get());
            message = "You successfully uploaded " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.OK).body(message);

        } catch (IOException e) {
            message = "FAIL to upload " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
        }

    }

}
