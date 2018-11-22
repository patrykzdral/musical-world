package com.patrykzdral.musicalworldcore.controller.user;

import com.patrykzdral.musicalworldcore.persistance.entity.User;
import com.patrykzdral.musicalworldcore.validation.exception.InternalException;
import com.patrykzdral.musicalworldcore.services.user.model.UserWithPhotoDTO;
import com.patrykzdral.musicalworldcore.services.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<User> deleteAccount(@RequestParam("username") String username) {
        User user = userService.deleteAccount(username);
        return ResponseEntity.ok(user);
    }

    @RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<User> updateAccount(@Valid @RequestBody UserWithPhotoDTO userDTO) {
        log.info(userDTO.toString());
        User user= userService.updateAccount(userDTO);
        return ResponseEntity.ok(user);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<User> findOne(@RequestParam("username") String username) {
        Optional<User> user = userService.findByUsername(username);
        if (!user.isPresent())
            throw new InternalException("User exception", "User does not exists");
        return ResponseEntity.ok(user.get());
    }

    @RequestMapping(value = "with-photo",method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<UserWithPhotoDTO> findUserWithPhoto(@RequestParam("username") String username) throws IOException {
        return ResponseEntity.ok(userService.findUserWithPhotoByUsername(username));
    }

    @RequestMapping(value = "photo", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseBody
    public ResponseEntity<String> findOne(@RequestParam("file") MultipartFile file, @RequestParam("username") String username) {
        String message=null;
        Optional<User> user = userService.findByUsername(username);
        if (!user.isPresent())
            throw new InternalException("User exception", "User does not exists");

        try {
            userService.saveUserPhoto(file, user.get());
            message = "You successfully uploaded " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.OK).body(message);

        } catch (IOException e) {
            message = "FAIL to upload " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
        }

    }



//    @RequestMapping(method = RequestMethod.GET)
//    @ResponseBody
//    public ResponseEntity<Iterable<User>> findAll() {
//        Iterable<User> user = userService.findAll();
//        return ResponseEntity.ok(user);
//    }
}
