package com.patrykzdral.musicalworldcore.controller.user_reference;

import com.patrykzdral.musicalworldcore.persistance.entity.UserReference;
import com.patrykzdral.musicalworldcore.services.user_reference.dto.UserReferenceDTO;
import com.patrykzdral.musicalworldcore.services.user_reference.service.UserReferenceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-references")
@Slf4j
public class UserReferenceController {

    private final
    UserReferenceService userReferenceService;

    @Autowired
    public UserReferenceController(UserReferenceService userReferenceService) {
        this.userReferenceService = userReferenceService;
    }

    @PostMapping(value = "save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserReference> saveUserReference(@RequestBody UserReferenceDTO userReferenceDTO, Authentication authentication) {
        log.info(userReferenceDTO.toString());
        UserReference userReference = userReferenceService.save(userReferenceDTO, ((User) authentication.getPrincipal()).getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(userReference);
    }

    @GetMapping(value = "find-user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserReferenceDTO>> getUserReferences(@RequestParam("username") String username) {
        List<UserReferenceDTO> userReferenceDTOS = userReferenceService.findAllUserReferences(username);
        log.info(userReferenceDTOS.toString());
        return ResponseEntity.status(HttpStatus.OK).body(userReferenceDTOS);
    }
}
