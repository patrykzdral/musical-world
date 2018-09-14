package com.patrykzdral.musicalworldcore.services.user.controller;

import com.patrykzdral.musicalworldcore.persistance.entity.User;
import com.patrykzdral.musicalworldcore.persistance.repository.UserRepository;
import com.patrykzdral.musicalworldcore.services.user.exception.ErrorResponse;
import com.patrykzdral.musicalworldcore.services.user.exception.InternalException;
import com.patrykzdral.musicalworldcore.services.user.model.RegisterUserRequestDTO;
import com.patrykzdral.musicalworldcore.services.user.service.impl.RegisterUserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@RestController
@Slf4j
public class UserController {

    private final RegisterUserServiceImpl registerUserService;

    @Autowired
    public UserController(RegisterUserServiceImpl registerUserServiceImpl) {
        this.registerUserService = registerUserServiceImpl;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/register")
    @ResponseBody
    public ResponseEntity<Object> save( @Valid @RequestBody RegisterUserRequestDTO registerUserRequestDTO) {
        User user = registerUserService.registerUserAccount(registerUserRequestDTO);
        return ResponseEntity.ok(user);
    }
}
