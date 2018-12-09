package com.patrykzdral.musicalworldcore.services.user_reference.service.impl;

import com.patrykzdral.musicalworldcore.persistance.entity.User;
import com.patrykzdral.musicalworldcore.persistance.entity.UserReference;
import com.patrykzdral.musicalworldcore.persistance.repository.UserReferenceRepository;
import com.patrykzdral.musicalworldcore.persistance.repository.UserRepository;
import com.patrykzdral.musicalworldcore.services.user_reference.dto.UserReferenceDTO;
import com.patrykzdral.musicalworldcore.services.user_reference.service.UserReferenceService;
import com.patrykzdral.musicalworldcore.validation.exception.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserReferenceServiceImpl implements UserReferenceService {

    private final
    UserReferenceRepository userReferenceRepository;

    private final
    UserRepository userRepository;

    private final
    Clock clock;
    @Autowired
    public UserReferenceServiceImpl(UserReferenceRepository userReferenceRepository, UserRepository userRepository, Clock clock) {
        this.userReferenceRepository = userReferenceRepository;
        this.userRepository = userRepository;
        this.clock = clock;
    }

    @Override
    public UserReference save(UserReferenceDTO userReferenceDTO, String username) {
        var userReference = new Object() {
            UserReference userReference;
        };
        Optional<User> optionalUserFrom = userRepository.findByUsername(username);
        Optional<User> optionalUserTo = userRepository.findByUsername(userReferenceDTO.getUserToUsername());
        if (optionalUserFrom.isPresent() && optionalUserTo.isPresent()) {

            Optional<UserReference> optionalUserReference = userReferenceRepository.findSameReference
                    (userReferenceDTO.getUserFromUsername(), userReferenceDTO.getUserToUsername());
            optionalUserReference.ifPresentOrElse(usrRef-> {
                throw new ApplicationException("User reference exception", "You has already given this user opinion");
            },()->{
                userReference.userReference = UserReference.builder()
                        .userFrom(optionalUserFrom.get())
                        .userTo(optionalUserTo.get())
                        .text(userReferenceDTO.getText())
                        .starRating(userReferenceDTO.getStarRating())
                        .referenceDate(ZonedDateTime.now(clock))
                        .build();
                userReferenceRepository.save(userReference.userReference);
            });

            return userReference.userReference;

        } else {
            throw new ApplicationException("User exception", "User does not exists");
        }
    }

    @Override
    public List<UserReferenceDTO> findAllUserReferences(String username) {
        List<UserReference> userReferences = userReferenceRepository.findAllUserReferences(username);
        return userReferences.stream().map(this::convertToUserReferenceDTO).collect(Collectors.toList());
    }

    private UserReferenceDTO convertToUserReferenceDTO(UserReference userReference){
        return UserReferenceDTO.builder()
                .userFromUsername(userReference.getUserFrom().getUsername())
                .userToUsername(userReference.getUserTo().getUsername())
                .text(userReference.getText())
                .starRating(userReference.getStarRating())
                .referenceDate(userReference.getReferenceDate())
                .build();
    }
}
