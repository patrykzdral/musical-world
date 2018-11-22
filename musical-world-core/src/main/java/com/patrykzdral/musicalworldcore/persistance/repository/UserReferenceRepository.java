package com.patrykzdral.musicalworldcore.persistance.repository;

import com.patrykzdral.musicalworldcore.persistance.entity.UserReference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserReferenceRepository extends JpaRepository<UserReference,Long> {
}
