package com.patrykzdral.musicalworldcore.persistance.repository;

import com.patrykzdral.musicalworldcore.persistance.entity.UserReference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface UserReferenceRepository extends JpaRepository<UserReference,Long> {

    @Query("Select u FROM UserReference u where u.userFrom.username=?1 and u.userTo.username=?2 ")
    Optional<UserReference> findSameReference(String userFromUsername, String userToUsername);

    @Query("Select u FROM UserReference u where u.userTo.username=?1")
    List<UserReference> findAllUserReferences(String username);

    @Query("Select u FROM UserReference u where u.userTo.username=?1 or u.userFrom.username=?1")
    List<UserReference> findByUser(String username);
}
