package com.patrykzdral.musicalworldcore.persistance.repository;

import com.patrykzdral.musicalworldcore.persistance.entity.Picture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PictureRepository extends JpaRepository<Picture,Long> {
}
