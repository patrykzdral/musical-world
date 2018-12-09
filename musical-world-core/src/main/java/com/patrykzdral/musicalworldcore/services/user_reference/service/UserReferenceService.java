package com.patrykzdral.musicalworldcore.services.user_reference.service;

import com.patrykzdral.musicalworldcore.persistance.entity.UserReference;
import com.patrykzdral.musicalworldcore.services.user_reference.dto.UserReferenceDTO;

import java.util.List;

public interface UserReferenceService {

    UserReference save(UserReferenceDTO file, String username);

    List<UserReferenceDTO> findAllUserReferences(String username);
}
