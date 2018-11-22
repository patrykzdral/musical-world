package com.patrykzdral.musicalworldcore.controller.picture;

import com.patrykzdral.musicalworldcore.persistance.entity.Picture;
import com.patrykzdral.musicalworldcore.services.picture.service.PictureService;
import com.patrykzdral.musicalworldcore.services.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/api/picture")
public class PictureController {
    private final
    PictureService pictureService;
    private final
    UserService userService;
    private List<String> files = new ArrayList<String>();

    @Autowired
    public PictureController(PictureService pictureService, UserService userService) {
        this.pictureService = pictureService;
        this.userService = userService;
    }

    @PostMapping(
            value = "/new",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE}
    )
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        log.info("FILE    "+ file.getName());
        String message = "";
        try {
            Picture pic = pictureService.save(file);
            message= pic.getFileName();
            return ResponseEntity.status(HttpStatus.OK).body(message);
        } catch (Exception e) {
            message = null;
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
        }
    }

    @PostMapping(value="user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> handleUserFileUpload(@RequestParam("file") MultipartFile file,
                                                       @RequestParam("username") String username) {
        log.info(username);
        log.info("FILE    "+ file.getName());
        String message = "";
        try {
            userService.assignPhotoToUser(file,username);
            pictureService.save(file);
            message = "You successfully uploaded " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.OK).body(message);
        } catch (Exception e) {
            message = "FAIL to upload " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
        }
    }

    @GetMapping("/get")
    public ResponseEntity<byte[]> getFile(@RequestParam Long id) {
        Optional<Picture> fileOptional = pictureService.findById(id);

        if(fileOptional.isPresent()) {
            Picture file = fileOptional.get();
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                    .body(file.getPic());
        }

        return ResponseEntity.status(404).body(null);
    }
}
