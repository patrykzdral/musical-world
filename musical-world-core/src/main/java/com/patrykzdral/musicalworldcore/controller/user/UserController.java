package com.patrykzdral.musicalworldcore.controller.user;

import com.patrykzdral.musicalworldcore.persistance.entity.User;
import com.patrykzdral.musicalworldcore.services.user.exception.InternalException;
import com.patrykzdral.musicalworldcore.services.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<User> findOne(@RequestParam("username") String username) {
        Optional<User> user = userService.findByUsername(username);
        if (!user.isPresent())
            throw new InternalException("User exception", "User does not exists");
        return ResponseEntity.ok(user.get());
    }

//    @RequestMapping(method = RequestMethod.GET)
//    @ResponseBody
//    public ResponseEntity<Iterable<User>> findAll() {
//        Iterable<User> user = userService.findAll();
//        return ResponseEntity.ok(user);
//    }
}
