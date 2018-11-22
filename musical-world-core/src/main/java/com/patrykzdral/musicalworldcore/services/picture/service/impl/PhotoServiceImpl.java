package com.patrykzdral.musicalworldcore.services.picture.service.impl;

import com.patrykzdral.musicalworldcore.persistance.entity.Picture;
import com.patrykzdral.musicalworldcore.persistance.repository.PictureRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.multipart.MultipartFile;
import com.patrykzdral.musicalworldcore.services.picture.service.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public class PhotoServiceImpl implements PictureService {
    private final PictureRepository pictureRepository;

    @Autowired
    public PhotoServiceImpl(PictureRepository pictureRepository) {
        this.pictureRepository = pictureRepository;
    }

    @Override
    public Picture save(MultipartFile file) throws IOException {
        String generatedString = RandomStringUtils.random(10, true, true);
        Picture pic = Picture.builder()
                        .fileName(generatedString)
                        .mimetype(file.getContentType())
                        .pic(file.getBytes())
                        .build();
        pictureRepository.save(pic);
        return pic;
    }

    @Override
    public Optional<Picture> findById(Long id) {
        return pictureRepository.findById(id);
    }
}
