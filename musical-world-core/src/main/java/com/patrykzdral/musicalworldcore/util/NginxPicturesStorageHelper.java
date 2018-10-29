package com.patrykzdral.musicalworldcore.util;


import com.patrykzdral.musicalworldcore.services.user.exception.InternalException;
import org.apache.commons.io.FilenameUtils;
import org.hibernate.id.UUIDGenerator;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class NginxPicturesStorageHelper {
    private NginxPicturesStorageHelper() {
    }
    public static String savePictureInDockerNginxServer(MultipartFile file, String imagesDockerVolumeLocation) {
        byte[] bytes;
        try {
            bytes = file.getBytes();
        } catch (IOException e) {
            throw new InternalException("Saving picture error", "Unable to read picture file: " + e.getMessage());
        }
        var filename = UUIDGenerator.buildSessionFactoryUniqueIdentifierGenerator() + "_" + System.currentTimeMillis()
                + "." + FilenameUtils.getExtension(file.getOriginalFilename());
        var filePath = imagesDockerVolumeLocation + filename;
        var path = Paths.get(filePath);
        try {
            Files.write(path, bytes);
        } catch (IOException e) {
            throw new InternalException("Saving picture error", "Unable to save picture in tmp localization: " + e.getMessage());
        }
        return filename;
    }
}
