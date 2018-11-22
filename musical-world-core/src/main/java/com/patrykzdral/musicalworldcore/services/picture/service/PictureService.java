package com.patrykzdral.musicalworldcore.services.picture.service;

import com.patrykzdral.musicalworldcore.persistance.entity.Concert;
import com.patrykzdral.musicalworldcore.persistance.entity.Picture;
import com.patrykzdral.musicalworldcore.services.concert.dto.ConcertDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

public interface PictureService {
    Picture save(MultipartFile file) throws IOException;
    Optional<Picture> findById(Long id);
}
